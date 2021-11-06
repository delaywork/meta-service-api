package com.meta.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.meta.mapper.VersionHistoryMapper;
import com.meta.model.pojo.VersionHistory;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Log4j2
@Service
public class VersionHistoryServiceImpl {

    @Autowired
    private VersionHistoryMapper versionHistoryMapper;

    /**
     * 新增历史版本
     * */
    @Transactional
    public void add(VersionHistory versionHistory){
        versionHistoryMapper.insert(versionHistory);
    }

    /**
     * 彻底删除历史版本
     * */
    @Transactional
    public void completeDelete(Long dataRoomId){
        QueryWrapper<VersionHistory> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(VersionHistory::getDataRoomId, dataRoomId);
        versionHistoryMapper.delete(wrapper);
    }

}
