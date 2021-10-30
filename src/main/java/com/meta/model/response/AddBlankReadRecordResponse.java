package com.meta.model.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;


@Data
@Builder
public class AddBlankReadRecordResponse {

    // 阅读记录
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long readScheduleId;

    // 阅读次数
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long readTimeId;

    @Tolerate
    public AddBlankReadRecordResponse(){}
}
