package com.meta.model.request;

import com.meta.model.enums.DataRoomTypeEnum;
import com.meta.model.enums.ShareSourceTypeEnum;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;

@Data
@Builder
public class GetRecordByReadSourceRequest {

    // 阅读源 id
    private Long sourceId;

    // 阅读源类型
    private ShareSourceTypeEnum sourceType;

    @Tolerate
    public GetRecordByReadSourceRequest(){}
}
