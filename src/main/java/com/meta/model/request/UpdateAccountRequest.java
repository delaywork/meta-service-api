package com.meta.model.request;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;
import org.springframework.web.bind.annotation.RequestHeader;

@Data
@Builder
public class UpdateAccountRequest {

    // 账户ID
    private Long accountId;

    // 用户名
    private String setName;

    // 用户密码
    private String setPassword;

    // 用户头像
    private String setAvatarUrl;

    // 时区
    private Integer setTimeZone;

    // 时区描述
    private String setTimeZoneText;

    // 语言类型
    private String setLanguageType;

    @Tolerate
    public UpdateAccountRequest(){}
}
