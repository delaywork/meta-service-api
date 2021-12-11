package com.meta.model.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;

@Data
@Builder
public class BindPhoneRequest {

    private String phonePrefix;

    private String phone;

    private String code;

    @ApiModelProperty(hidden = true)
    private Long accountId;

    @ApiModelProperty(hidden = true)
    private Long tenantId;

    @Tolerate
    public BindPhoneRequest(){}
}
