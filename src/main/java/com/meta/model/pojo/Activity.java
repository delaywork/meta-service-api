package com.meta.model.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.FastjsonTypeHandler;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.meta.model.BaseModel;
import com.meta.model.enums.ActivityOperationResourceEnum;
import com.meta.model.enums.ActivityOperationTypeEnum;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;

import java.util.HashMap;

/**
 * @Author Martin
 */
@Data
@Builder
@TableName(value="activity", autoResultMap = true)
public class Activity extends BaseModel {

    // 主键ID 使用雪花算法生成
    @TableId(value = "id",type = IdType.NONE)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;

    // 资源
    @TableField(value = "operation_resource")
    private ActivityOperationResourceEnum operationResource;

    // 资源id
    @TableField(value = "operation_resource_id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long operationResourceId;

    // 操作类型
    @TableField(value = "operation_type")
    private ActivityOperationTypeEnum operationType;

    // 账户id
    @TableField(value = "operation_account")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long operationAccount;

    // 授权内容
    @TableField(value = "detail", typeHandler = FastjsonTypeHandler.class)
    private HashMap<String, String> detail;

    // 租户id
    @TableField(value = "tenant_id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long tenantId;

    @Tolerate
    public Activity(){}

}
