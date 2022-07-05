package com.meta.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.google.common.collect.ObjectArrays;
import com.meta.mapper.ReadScheduleMapper;
import com.meta.mapper.ReadTimesMapper;
import com.meta.model.ErrorEnum;
import com.meta.model.FastRunTimeException;
import com.meta.model.pojo.ReadSchedule;
import com.meta.model.pojo.ReadTime;
import com.meta.model.pojo.ReadTimeDetail;
import com.meta.model.request.UpdateReadRecordRequest;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Service
@Log4j2
public class ReadRecordServiceImpl {

    @Resource
    private ReadTimesMapper readTimesMapper;
    @Resource
    private ReadScheduleMapper readScheduleMapper;

    /**
     * 更新阅读记录
     * */
    public void updateReadRecord(UpdateReadRecordRequest request){
        if (ObjectUtils.isEmpty(request.getSourceId()) || ObjectUtils.isEmpty(request.getAccountId()) || ObjectUtils.isEmpty(request.getPageIndex()) || ObjectUtils.isEmpty(request.getTimes()) || ObjectUtils.isEmpty(request.getStartTime()) || ObjectUtils.isEmpty(request.getWebSocketSessionId())){
            throw new FastRunTimeException(ErrorEnum.参数不正确);
        }

        // 更新读进度记录
        QueryWrapper<ReadSchedule> readScheduleQueryWrapper = new QueryWrapper<>();
        readScheduleQueryWrapper.lambda().eq(ReadSchedule::getSourceId, request.getSourceId())
                .eq(ReadSchedule::getAccountId, request.getAccountId())
                .eq(ReadSchedule::getDataIsDeleted, false)
                .last("limit 1");
        ReadSchedule readSchedule = readScheduleMapper.selectOne(readScheduleQueryWrapper);
        if (ObjectUtils.isEmpty(readSchedule)){
            log.info("新增 ReadSchedule，SourceId:{}, AccountId:{}, PageIndex:{}, Times:{}", request.getSourceId(), request.getAccountId(), request.getPageIndex(), request.getTimes());
            readSchedule = ReadSchedule.builder().sourceId(request.getSourceId())
                    .sourceType(request.getSourceType())
                    .accountId(request.getAccountId())
                    .accountType(request.getAccountType())
                    .firstTime(request.getStartTime())
                    .pageIndex(request.getPageIndex())
                    .lastTime(request.getStartTime())
                    .readTime(request.getTimes()).build();
            readScheduleMapper.insert(readSchedule);
        }else{
            log.info("更新 ReadSchedule，SourceId:{}, AccountId:{}, PageIndex:{}, Times:{}", request.getSourceId(), request.getAccountId(), request.getPageIndex(), request.getTimes());
            UpdateWrapper<ReadSchedule> readScheduleUpdateWrapper = new UpdateWrapper<>();
            readScheduleUpdateWrapper.lambda().eq(ReadSchedule::getId, readSchedule.getId())
                    .set(ReadSchedule::getPageIndex, request.getPageIndex())
                    .set(ReadSchedule::getLastTime, request.getStartTime())
                    .set(ReadSchedule::getReadTime, readSchedule.getReadTime()+request.getTimes());
            readScheduleMapper.update(ReadSchedule.builder().build(), readScheduleUpdateWrapper);
        }

        // 查询是否存在同一 WebSocketSessionId 的阅读记录
        QueryWrapper<ReadTime> readTimeQueryWrapper = new QueryWrapper<>();
        readTimeQueryWrapper.lambda().eq(ReadTime::getWebSocketSessionId, request.getWebSocketSessionId())
                .eq(ReadTime::getSourceId, request.getSourceId())
                .eq(ReadTime::getAccountId, request.getAccountId())
                .eq(ReadTime::getDataIsDeleted, false)
                .last("limit 1");
        ReadTime readTime = readTimesMapper.selectOne(readTimeQueryWrapper);
        if (ObjectUtils.isEmpty(readTime)){
            log.info("长连接还未关联阅读记录，新建, SourceId:{}, AccountId:{}, PageIndex:{}, Times:{}", request.getSourceId(), request.getAccountId(), request.getPageIndex(), request.getTimes());
            List<ReadTimeDetail> readTimeDetails = new ArrayList<>();
            ReadTimeDetail readTimeDetail = ReadTimeDetail.builder().pageIndex(request.getPageIndex()).readTime(request.getTimes()).build();
            readTimeDetails.add(readTimeDetail);
            readTime = ReadTime.builder().sourceId(request.getSourceId())
                    .sourceType(request.getSourceType())
                    .accountId(request.getAccountId())
                    .accountType(request.getAccountType())
                    .startTime(request.getStartTime())
                    .readTime(request.getTimes())
                    .webSocketSessionId(request.getWebSocketSessionId())
                    .detail(readTimeDetails).build();
            readTimesMapper.insert(readTime);
        }else{
            log.info("更新阅读记录, SourceId:{}, AccountId:{}, PageIndex:{}, Times:{}", request.getSourceId(), request.getAccountId(), request.getPageIndex(), request.getTimes());
            List<ReadTimeDetail> readTimeDetails = readTime.getDetail();
            AtomicReference<Boolean> flag = new AtomicReference<>(true);
            readTimeDetails.forEach((readTimeDetail) -> {
                if (readTimeDetail.getPageIndex() == request.getPageIndex()){
                    readTimeDetail.setReadTime(readTimeDetail.getReadTime()+request.getTimes());
                    flag.set(false);
                }
            });
            if (flag.get()){
                ReadTimeDetail readTimeDetail = ReadTimeDetail.builder().readTime(request.getTimes()).pageIndex(request.getPageIndex()).build();
                readTimeDetails.add(readTimeDetail);
            }
            UpdateWrapper<ReadTime> readTimeUpdateWrapper = new UpdateWrapper<>();

            readTimeUpdateWrapper.lambda().eq(ReadTime::getId, readTime.getId())
                    .set(ReadTime::getDetail, JSON.toJSONString(readTimeDetails))
                    .set(ReadTime::getReadTime, readTime.getReadTime()+request.getTimes());
            readTimesMapper.update(new ReadTime(), readTimeUpdateWrapper);
        }
    }

}
