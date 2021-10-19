package com.meta.model.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.meta.model.BaseModel;
import com.meta.model.enums.AccountTypeEnum;
import com.meta.model.enums.DataRoomTypeEnum;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;


/**
 * @Author Martin
 * 阅读进度表
 */
@Data
@Builder
@TableName(value="read_schedule")
public class ReadSchedule extends BaseModel {

    // 主键ID 使用雪花算法生成
    @TableId(value = "id",type = IdType.NONE)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;

    // 阅读源 id
    @TableField(value = "source_id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long sourceId;

    // 阅读源类型
    @TableField(value = "source_type")
    private DataRoomTypeEnum sourceType;

    // 当前阅读页
    @TableField(value = "current_page")
    private Integer currentPage;

    // 阅读人员 id
    @TableField(value = "account_id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long accountId;

    // 阅读人员类型
    @TableField(value = "account_type")
    private AccountTypeEnum accountType;

    @Tolerate
    public ReadSchedule(){}

}
