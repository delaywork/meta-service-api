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
@TableName(value="info")
public class Info extends BaseModel {

    // 主键ID 使用雪花算法生成
    @TableId(value = "id",type = IdType.NONE)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;

    // 关于
    @TableField(value = "about")
    private String about;

    // 条款
    @TableField(value = "terms_conditions")
    private String termsAndConditions;

    // 策略
    @TableField(value = "policy")
    private String policy;

    @Tolerate
    public Info(){}

}
