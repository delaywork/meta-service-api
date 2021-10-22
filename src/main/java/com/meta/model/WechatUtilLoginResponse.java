package com.meta.model;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;

import java.io.Serializable;

/**
 * @Author Martin
 */
@Data
@Builder
public class WechatUtilLoginResponse {

    // 用户唯一标识
    private String openid;

    // 会话密钥
    private String session_key;

    // 开放平台唯一标识
    private String unionid;

    @Tolerate
    public WechatUtilLoginResponse(){}
}
