package com.meta.model.request;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;

@Data
@Builder
public class ReadRecordWebSocketRequest {

    // 阅读记录
    private Long readScheduleId;

    // 阅读次数
    private Long readTimeId;

    // 当前阅读页
    private Integer currentPage;

    // 阅读时长
    private Long times;

    @Tolerate
    public ReadRecordWebSocketRequest(){}
}
