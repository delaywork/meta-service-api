package com.meta.model.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.meta.model.BaseModel;
import com.meta.model.enums.AccountSexEnum;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;

import java.sql.Timestamp;

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

    // 用户名
    @TableField(value = "name")
    private String name;

    // 性别
    @TableField(value = "sex")
    private AccountSexEnum sex;

    // 生日
    @TableField(value = "birthday")
    private Timestamp birthday;

    // 邮箱
    @TableField(value = "email")
    private String email;

    // 手机前缀
    @TableField(value = "area_code")
    private String areaCode;

    // 手机
    @TableField(value = "phone")
    private String phone;

    // 用户密码
    @TableField(value = "password")
    private String password;

    // 用户头像
    @TableField(value = "avatar_url")
    private String avatarUrl;

    //时区配置
    @TableField(value = "time_zone")
    private Integer timeZone;

    //时区名称
    @TableField(value = "time_zone_text")
    private String timeZoneText;

    //语言配置
    @TableField(value = "language_type")
    private String languageType;

    // 租户id
    @TableField(value = "tenant_id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long tenantId;

    @Tolerate
    public Account(){}

}
