package com.meta.model.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.meta.model.BaseModel;
import com.meta.model.enums.DocumentCloudEnum;
import com.meta.model.enums.DocumentOperationTypeEnum;
import com.meta.model.enums.DocumentTypeEnum;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;


/**
 * @Author Martin
 */
@Data
@Builder
@TableName(value="document_undo_log")
public class DocumentUndoLog extends BaseModel {

    // 主键ID 使用雪花算法生成
    @TableId(value = "id",type = IdType.NONE)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;

    // 文件操作类型
    @TableField(value = "document_operation_type")
    private DocumentOperationTypeEnum documentOperationType;

    // 文件 id
    @TableField(value = "document_id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long documentId;

    // 父节点（跟节点不存在父节点）
    @TableField(value = "document_parent_id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long documentParentId;

    // 类型（pdf、文件夹...）
    @TableField(value = "document_type")
    private DocumentTypeEnum documentType;

    // 名称
    @TableField(value = "document_name")
    private String documentName;

    // 描述
    @TableField(value = "document_comments")
    private String documentComments;

    // 云
    @TableField(value = "document_cloud")
    private DocumentCloudEnum documentCloud;

    // 文件地址（类型为"文件夹"则为空）
    @TableField(value = "document_url")
    private String documentUrl;

    // 操作人员id
    @TableField(value = "operation_account_id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long operationAccountId;

    @Tolerate
    public DocumentUndoLog(){}

}
