package com.meta.model.request;

import com.meta.model.enums.ShareSourceTypeEnum;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;

@Data
@Builder
public class ReadRecordRequest {

    // 阅读源 id
    private Long sourceId;

    // 阅读源类型
    private ShareSourceTypeEnum sourceType;

    // 当前阅读页
    private Integer currentPage;

    @Tolerate
    public ReadRecordRequest(){}
}
