package com.meta.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.meta.mapper.ReadTimesMapper;
import com.meta.model.pojo.ReadTime;
import com.meta.model.request.AddReadTimeRequest;
import com.meta.model.request.GetReadTimeRequest;
import com.meta.model.request.UpdateReadTimeRequest;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class ReadTimeServiceImpl {

    @Autowired
    private ReadTimesMapper readTimesMapper;

    @Value("${reading.time:3}")
    private Long readingTime;

    /**
     * 新增阅读次数
     * */
    public ReadTime add(AddReadTimeRequest request){
        JSONObject json = new JSONObject();
        json.put(request.getCurrentPage().toString(), readingTime);
        String jsonString = json.toString();
        ReadTime readTime = ReadTime.builder().sourceId(request.getSourceId()).sourceType(request.getSourceType())
                .accountId(request.getAccountId()).accountType(request.getAccountType()).readContent(jsonString)
                .startTime(request.getStartTime()).readTime(readingTime).build();
        readTimesMapper.insert(readTime);
        return readTime;
    }

    /**
     * 更新阅读次数
     * */
    public ReadTime update(UpdateReadTimeRequest request){
        String readContent = request.getReadContent();
        JSONObject json = JSONObject.parseObject(readContent);
        if (json.containsKey(request.getCurrentPage().toString())){
            // 更新这一页的阅读时长
            Long time = json.getLong(request.getCurrentPage().toString());
            json.put(request.getCurrentPage().toString(), time+readingTime);
        }else{
            // 新增这一页的阅读信息
            json.put(request.getCurrentPage().toString(), readingTime);
        }
        String jsonString = json.toString();
        ReadTime readTime = ReadTime.builder().sourceId(request.getSourceId()).sourceType(request.getSourceType())
                .accountId(request.getAccountId()).accountType(request.getAccountType()).readContent(jsonString)
                .startTime(request.getStartTime()).readTime(readingTime).build();
        readTimesMapper.updateById(readTime);
        return readTime;
    }

    /**
     * 查询最后一次阅读次数
     * */
    public ReadTime lastReadTime(GetReadTimeRequest request, Long accountId){
        QueryWrapper<ReadTime> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(ReadTime::getSourceId, request.getSourceId())
                .eq(ReadTime::getSourceType, request.getSourceType())
                .eq(ReadTime::getAccountId, accountId)
                .eq(ReadTime::getDataIsDeleted, false)
                .orderByDesc(ReadTime::getId);
        ReadTime readTime = readTimesMapper.selectOne(wrapper);
        return readTime;
    }


}
