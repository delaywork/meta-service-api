package com.meta.model;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.sql.Timestamp;

/**
 * @author Martin
 */
@Data
public class BaseModel {

    // 数据创建日期
    @TableField(value = "data_create_time", fill = FieldFill.INSERT)
    private Long dataCreateTime;

    // 数据修改日期
    @TableField(value = "data_update_time", fill = FieldFill.UPDATE)
    private Long dataUpdateTime;

    // 数据逻辑删除标识位
    @TableField(value = "data_is_deleted", fill = FieldFill.INSERT)
    private Boolean dataIsDeleted = false;

    public Boolean getDataIsDeleted() {
        return this.dataIsDeleted;
    }
}
