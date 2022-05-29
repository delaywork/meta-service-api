package com.meta.model.request;

import com.baomidou.mybatisplus.annotation.TableField;
import com.meta.model.enums.AccountSexEnum;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;
import org.springframework.web.bind.annotation.RequestHeader;

import java.sql.Timestamp;

@Data
@Builder
public class UpdateAccountRequest {

    // 账户ID
    private Long accountId;

    // 用户名
    private String setName;

    // 性别
    private AccountSexEnum sex;

    // 生日
    private Timestamp birthday;

    // 用户密码
    private String password;

    // 用户头像
    private String avatarUrl;

    // 时区
    private Integer timeZone;

    // 时区描述
    private String timeZoneText;

    // 语言类型
    private String languageType;

    @Tolerate
    public UpdateAccountRequest(){}
}
