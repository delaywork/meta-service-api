package com.meta.service;

import com.meta.mapper.DataRoomMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class DataRoomServiceImpl {

    @Autowired
    private DataRoomMapper dataRoomMapper;

    /**
     * 新增文件
     */

    /**
     * 批量新增文件
     * */

    /**
     * 新增文件夹
     * */

    /**
     * 修改文件（将历史记录转移到 VersionHistory）
     * */

    /**
     * 删除文件/文件夹（删除文件会同步删除分享、历史版本）
     * */

    /**
     * 初始化跟目录
     * */

    /**
     * 根据id查询文件/文件夹（如果是文件夹会查询里面的内容）
     * */

    /**
     * 列表查询文件/文件夹
     * */

}
