package com.meta.model.request;

import com.meta.model.enums.DocumentTypeEnum;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;

@Data
@Builder
public class QueryReadTimeRequest {

    // 阅读源 id
    private Long sourceId;

    // 阅读源类型
    private DocumentTypeEnum sourceType;

    @Tolerate
    public QueryReadTimeRequest(){}
}
