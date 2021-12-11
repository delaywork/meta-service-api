package com.meta.model.request;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;

@Data
@Builder
public class LoginByWechatSecretRequest {

    private String requestData;

    @Tolerate
    public LoginByWechatSecretRequest(){}
}
