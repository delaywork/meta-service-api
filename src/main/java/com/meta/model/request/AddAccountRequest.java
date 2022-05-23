package com.meta.model.request;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.meta.model.enums.AccountTypeEnum;
import com.meta.model.enums.ShareSourceTypeEnum;
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
