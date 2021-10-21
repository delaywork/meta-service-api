package com.meta.service;

import com.meta.mapper.VersionHistoryMapper;
import com.meta.model.pojo.VersionHistory;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class VersionHistoryServiceImpl {

    @Autowired
    private VersionHistoryMapper versionHistoryMapper;

    /**
     * 新增历史版本
     * */
    public void add(VersionHistory versionHistory){
        versionHistoryMapper.insert(versionHistory);
    }

}
