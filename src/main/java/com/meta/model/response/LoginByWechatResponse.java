package com.meta.model.response;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;

import java.io.Serializable;

/**
 * @Author Martin
 */
@Data
@Builder
public class LoginByWechatResponse implements Serializable {

    // 是否进行过手机号验证
    private Boolean phoneAuth;

    private String accessToken;

    private String refreshToken;

    @Tolerate
    public LoginByWechatResponse(){}
}
