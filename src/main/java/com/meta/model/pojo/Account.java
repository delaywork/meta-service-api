package com.meta.model.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.meta.model.BaseModel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;

/**
 * @Author Martin
 */
@Data
@Builder
@TableName(value="account")
public class Account extends BaseModel {

    // 主键ID 使用雪花算法生成
    @TableId(value = "id",type = IdType.NONE)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;

    // openid
    @TableField(value = "openid")
    private String openid;

    // unionid
    @TableField(value = "unionid")
    private String unionid;

    // 微信认证状态（1：进行过微信认证， 0：未进行过微信认证）
    @TableField(value = "wechat_auth")
    private Boolean wechatAuth;

    // 用户名
    @TableField(value = "name")
    private String name;

    // 邮箱
    @TableField(value = "email")
    private String email;

    // 手机前缀
    @TableField(value = "phone_prefix")
    private String phonePrefix;

    // 手机
    @TableField(value = "phone")
    private String phone;

    // 用户密码
    @TableField(value = "password")
    private String password;

    // 用户头像
    @TableField(value = "avatar_url")
    private String avatarUrl;

    // 用户密码盐
    @TableField(value = "password_salt")
    private String passwordSalt;

    // 租户id
    @TableField(value = "tenant_id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long tenantId;

    @Tolerate
    public Account(){}

}
