package com.meta.model.request;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;

@Data
@Builder
public class AddAccountRequest {

    // openid
    private String openid;

    // unionid
    private String unionid;

    // 用户名
    private String name;

    // 用户密码
    private String password;

    // 用户头像
    private String avatarUrl;

    @Tolerate
    public AddAccountRequest(){}
}
