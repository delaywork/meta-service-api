package com.meta.model.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.meta.model.BaseModel;
import com.meta.model.enums.InfoEnabledEnum;
import com.meta.model.enums.InfoTypeEnum;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;

/**
 * @Author Martin
 */
@Data
@Builder
@TableName(value="info")
public class Info extends BaseModel {

    // 主键ID 使用雪花算法生成
    @TableId(value = "id",type = IdType.NONE)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;

    // 内容
    @TableField(value = "context")
    private String context;

    // 类型
    @TableField(value = "type")
    private InfoTypeEnum type;

    // 是否使用
    @TableField(value = "enabled")
    private InfoEnabledEnum enabled;

    @Tolerate
    public Info(){}

}
