package com.meta.model.request;

import com.meta.model.enums.SourceTypeEnum;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;

@Data
@Builder
public class ReadRecordWebSocketRequest {

    // 阅读源 id
    private Long sourceId;

    // 阅读源类型
    private SourceTypeEnum sourceType;

    // 当前阅读页
    private Long pageIndex;

    // 阅读人的 token
    private String authorization;

    @Tolerate
    public ReadRecordWebSocketRequest(){}
}
