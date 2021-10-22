package com.meta.model.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.meta.model.BaseModel;
import com.meta.model.enums.DataRoomCloudEnum;
import com.meta.model.enums.DataRoomTypeEnum;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;


/**
 * @Author Martin
 */
@Data
@Builder
@TableName(value="data_room")
public class DataRoom extends BaseModel {

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
    private DataRoomTypeEnum type;

    // 名称
    @TableField(value = "name")
    private String name;

    // 云存储中的唯一命名
    @TableField(value = "cloud_key")
    private String cloudKey;

    // 描述
    @TableField(value = "describe")
    private String describe;

    // 云
    @TableField(value = "cloud")
    private DataRoomCloudEnum cloud;

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
    public DataRoom(){}

}
