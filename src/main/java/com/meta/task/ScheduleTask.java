package com.meta.task;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.meta.mapper.DataRoomMapper;
import com.meta.mapper.VersionHistoryMapper;
import com.meta.model.pojo.DataRoom;
import com.meta.model.pojo.VersionHistory;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;

@Configuration
@EnableScheduling
public class ScheduleTask {

    @Autowired
    private DataRoomMapper dataRoomMapper;
    @Autowired
    private VersionHistoryMapper versionHistoryMapper;

    // 30天
    private final static long THIRTY_DAYS_AGO = 1000l * 60l * 60l * 24l * 30l;

    @Scheduled(cron = "0 0 4 * * ?")
    private void dataRoomCompleteDeleteTask() {
        Long thirtyDysAgo = System.currentTimeMillis() - THIRTY_DAYS_AGO;
        // 彻底删除30天前的DataRoom
        QueryWrapper<DataRoom> dataRoomWrapper = new QueryWrapper<>();
        dataRoomWrapper.lambda().eq(DataRoom::getDataIsDeleted, true).lt(DataRoom::getDataUpdateTime, thirtyDysAgo);
        List<DataRoom> dataRooms = dataRoomMapper.selectList(dataRoomWrapper);
        dataRoomMapper.delete(dataRoomWrapper);
        // 彻底删除历史版本
        if (ObjectUtils.isNotEmpty(dataRooms)){
            dataRooms.stream().forEach(item -> {
                QueryWrapper<VersionHistory> wrapper = new QueryWrapper<>();
                wrapper.lambda().eq(VersionHistory::getDataRoomId, item.getId());
                versionHistoryMapper.delete(wrapper);
            });
        }
    }
}