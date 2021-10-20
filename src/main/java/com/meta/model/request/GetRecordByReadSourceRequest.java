package com.meta.model.request;

import com.meta.model.enums.DataRoomTypeEnum;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;

@Data
@Builder
public class GetRecordByReadSourceRequest {

    // 阅读源 id
    private Long sourceId;

    // 阅读源类型
    private DataRoomTypeEnum sourceType;

    @Tolerate
    public GetRecordByReadSourceRequest(){}
}
