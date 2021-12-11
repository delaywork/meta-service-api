package com.meta.model.request;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;

@Data
@Builder
public class LoginByWechatRequest {

    private String openid;
    private String unionid;
    private String name;

    @Tolerate
    public LoginByWechatRequest(){}
}
