package com.meta.model.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.meta.model.BaseModel;
import com.meta.model.enums.AccountAuthorizationTypeEnum;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;

/**
 * @Author Martin
 */
@Data
@Builder
@TableName(value="account_authorization")
public class AccountAuthorization extends BaseModel {

    // 主键ID 使用雪花算法生成
    @TableId(value = "id",type = IdType.NONE)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;

    // 账户 id
    @TableField(value = "account_id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long accountId;

    // 授权类型
    @TableField(value = "type")
    private AccountAuthorizationTypeEnum type;

    // 授权内容
    @TableField(value = "context")
    private String context;

    // 租户id
    @TableField(value = "tenant_id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long tenantId;

    @Tolerate
    public AccountAuthorization(){}

}
