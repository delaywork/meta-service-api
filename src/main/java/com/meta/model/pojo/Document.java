package com.meta.model.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.meta.model.BaseModel;
import com.meta.model.enums.DocumentCloudEnum;
import com.meta.model.enums.DocumentTypeEnum;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;


/**
 * @Author Martin
 */
@Data
@Builder
@TableName(value="document")
public class Document extends BaseModel {

    // 主键ID 使用雪花算法生成
    @TableId(value = "id",type = IdType.NONE)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;

    // 父节点（跟节点不存在父节点）
    @TableField(value = "parent_id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long parentId;

    // 类型（pdf、文件夹...）
    @TableField(value = "type")
    private DocumentTypeEnum type;

    // 名称
    @TableField(value = "name")
    private String name;

    // 描述
    @TableField(value = "comments")
    private String comments;

    // 云
    @TableField(value = "cloud")
    private DocumentCloudEnum cloud;

    // 文件地址（类型为"文件夹"则为空）
    @TableField(value = "url")
    private String url;

    // 租户id
    @TableField(value = "tenant_id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long tenantId;

    // 操作人员id
    @TableField(value = "operation_account_id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long operationAccountId;

    @Tolerate
    public Document(){}

}
