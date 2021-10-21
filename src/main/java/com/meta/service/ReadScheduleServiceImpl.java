package com.meta.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.meta.mapper.ReadScheduleMapper;
import com.meta.model.pojo.ReadSchedule;
import com.meta.model.pojo.ReadTime;
import com.meta.model.request.*;
import com.meta.model.response.ReadRecordResponse;
import com.meta.model.response.vo.ReadTimeVO;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.*;

@Log4j2
@Service
public class ReadScheduleServiceImpl {

    @Autowired
    private ReadScheduleMapper readScheduleMapper;
    @Autowired
    private ReadTimeServiceImpl readTimeService;

    @Value("${reading.time:3}")
    private Long readingTime;

    /**
     * 记录阅读数据
     * */
    public void readRecord(ReadRecordRequest request, Long accountId){
        // 查询是否存在历史阅读数据
        ReadSchedule readSchedule = this.getReadScheduleByReadSource(GetRecordByReadSourceRequest.builder().sourceId(request.getSourceId()).sourceType(request.getSourceType()).build(), accountId);
        if (ObjectUtils.isEmpty(readSchedule)){
            // 新增阅读记录
            readSchedule = ReadSchedule.builder().sourceId(request.getSourceId()).sourceType(request.getSourceType()).currentPage(request.getCurrentPage()).accountId(accountId).build();
            this.add(readSchedule);
        }else{
            // 更新阅读记录
            readSchedule.setCurrentPage(request.getCurrentPage());
            this.update(readSchedule);
        }
    }

    /**
     * 根据阅读源、阅读源类型、用户查询阅读记录
     * */
    public ReadSchedule getReadScheduleByReadSource(GetRecordByReadSourceRequest request, Long accountId){
        QueryWrapper<ReadSchedule> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(ReadSchedule::getSourceId, request.getSourceId())
                .eq(ReadSchedule::getSourceType, request.getSourceType())
                .eq(ReadSchedule::getAccountId, accountId)
                .eq(ReadSchedule::getDataIsDeleted, false);
        ReadSchedule readSchedule = readScheduleMapper.selectOne(wrapper);
        return readSchedule;
    }

    /**
     * 新增阅读记录
     * */
    private ReadSchedule add(ReadSchedule readSchedule){
        long currentTimes = System.currentTimeMillis();
        // 新增阅读次数
        AddReadTimeRequest addReadTimeRequest = AddReadTimeRequest.builder().sourceId(readSchedule.getSourceId()).sourceType(readSchedule.getSourceType())
                .accountId(readSchedule.getAccountId()).accountType(readSchedule.getAccountType()).currentPage(readSchedule.getCurrentPage()).startTime(currentTimes).build();
        readTimeService.add(addReadTimeRequest);
        // 添加阅读记录信息
        readSchedule.setFirstTime(currentTimes);
        readSchedule.setLastTime(currentTimes);
        readSchedule.setReadTime(readingTime);
        readScheduleMapper.insert(readSchedule);
        return readSchedule;
    }

    /**
     * 更新阅读记录
     * */
    private ReadSchedule update(ReadSchedule readSchedule){
        long currentTimes = System.currentTimeMillis();
        // 判断是否是一次新的阅读
        if (readSchedule.getLastTime() + readingTime * 5 < currentTimes){
            log.info("一次新的阅读记录，accountId:{}, sourceId:{}, sourceType:{}",readSchedule.getAccountId(), readSchedule.getSourceId(), readSchedule.getSourceType());
            // 新增阅读次数
            AddReadTimeRequest addReadTimeRequest = AddReadTimeRequest.builder().sourceId(readSchedule.getSourceId()).sourceType(readSchedule.getSourceType())
                    .accountId(readSchedule.getAccountId()).accountType(readSchedule.getAccountType()).currentPage(readSchedule.getCurrentPage()).startTime(currentTimes).build();
            readTimeService.add(addReadTimeRequest);
        }else{
            log.info("不是新的阅读记录，accountId:{}, sourceId:{}, sourceType:{}",readSchedule.getAccountId(), readSchedule.getSourceId(), readSchedule.getSourceType());
            // 查询最后一次阅读次数进行更新
            ReadTime readTime = readTimeService.lastReadTime(GetReadTimeRequest.builder().sourceId(readSchedule.getSourceId()).sourceType(readSchedule.getSourceType()).build(), readSchedule.getAccountId());
            UpdateReadTimeRequest updateReadTimeRequest = UpdateReadTimeRequest.builder().sourceId(readSchedule.getSourceId()).sourceType(readSchedule.getSourceType())
                    .accountId(readSchedule.getAccountId()).accountType(readSchedule.getAccountType()).currentPage(readSchedule.getCurrentPage()).startTime(readTime.getStartTime())
                    .id(readTime.getId()).readContent(readTime.getReadContent()).build();
            readTimeService.update(updateReadTimeRequest);
        }
        // 更新阅读记录
        readSchedule.setLastTime(currentTimes);
        Long readTime = readSchedule.getReadTime();
        readSchedule.setReadTime(readTime + readingTime);
        readScheduleMapper.updateById(readSchedule);
        return readSchedule;
    }

    /**
     * 阅读记录全量查询
     * */
    public ReadRecordResponse queryReadRecord(GetRecordByReadSourceRequest request, Long accountId){
        ReadRecordResponse response = new ReadRecordResponse();
        // 查询阅读记录
        QueryWrapper<ReadSchedule> readScheduleQueryWrapper = new QueryWrapper<>();
        readScheduleQueryWrapper.lambda().eq(ReadSchedule::getSourceId, request.getSourceId())
                .eq(ReadSchedule::getSourceType, request.getSourceType())
                .eq(ReadSchedule::getAccountId, accountId)
                .eq(ReadSchedule::getDataIsDeleted, false);
        ReadSchedule readSchedule = readScheduleMapper.selectOne(readScheduleQueryWrapper);
        // 查询阅读次数
        List<ReadTime> readTimes = readTimeService.queryReadTime(QueryReadTimeRequest.builder().build(), accountId);
        List<ReadTimeVO> readTimeVOS = new ArrayList<>();
        JSONObject pageReadTime = new JSONObject();
        if (!ObjectUtils.isEmpty(readTimes)){
            // 汇总每一页的阅读时长
            readTimes.stream().forEach(item->{
                ReadTimeVO readTimeVO = ReadTimeVO.builder().id(item.getId()).startTime(item.getStartTime()).readTime(item.getReadTime()).build();
                if (StringUtils.isNoneEmpty(item.getReadContent())){
                    JSONObject json = JSONObject.parseObject(item.getReadContent());
                    Set<String> jsonKeys = json.keySet();
                    jsonKeys.stream().forEach(key->{
                        Long newTime = json.getLong(key);
                        if (pageReadTime.containsKey(key)){
                            // key 存在值累加
                            Long oldTime = pageReadTime.getLong(key);
                            pageReadTime.put(key,oldTime+newTime);
                        }else{
                            // key 不存在新增
                            pageReadTime.put(key,newTime);
                        }
                    });
                    readTimeVO.setReadContent(json);
                }
                readTimeVOS.add(readTimeVO);
            });
        }
        response.setSourceId(readSchedule.getSourceId());
        response.setSourceType(readSchedule.getSourceType());
        response.setAccountId(readSchedule.getAccountId());
        response.setAccountType(readSchedule.getAccountType());
        response.setReadTime(readSchedule.getReadTime());
        response.setFirstTime(readSchedule.getFirstTime());
        response.setLastTime(readSchedule.getLastTime());
        response.setCurrentPage(readSchedule.getCurrentPage());
        response.setPageReadTime(pageReadTime);
        response.setReadTimeList(readTimeVOS);
        return response;
    }


}
