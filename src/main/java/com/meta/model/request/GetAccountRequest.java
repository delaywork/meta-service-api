package com.meta.model.request;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;

@Data
@Builder
public class GetAccountRequest {

    private Long accountId;

    private String openid;

    private String unionid;

    @Tolerate
    public GetAccountRequest(){}
}
