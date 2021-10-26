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
 * dataRoom 历史版本
 */
@Data
@Builder
@TableName(value="version_history")
public class VersionHistory extends BaseModel {

    // 主键ID 使用雪花算法生成
    @TableId(value = "id",type = IdType.NONE)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;

    // 最新 dataRoom 文件
    @TableField(value = "data_room_id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long dataRoomId;

    // 类型（pdf、文件夹...）
    @TableField(value = "type")
    private DataRoomTypeEnum type;

    // 名称
    @TableField(value = "name")
    private String name;

    // 描述
    @TableField(value = "note")
    private String note;

    // 文件地址（类型为"文件夹"则为空）
    @TableField(value = "url")
    private String url;

    // 云
    @TableField(value = "cloud")
    private DataRoomCloudEnum cloud;

    // 租户id
    @TableField(value = "tenant_id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long tenantId;

    // 操作人员id
    @TableField(value = "operation_account_id")
    private Long operationAccountId;

    @Tolerate
    public VersionHistory(){}

}
