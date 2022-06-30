package com.meta.model.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.meta.model.BaseModel;
import com.meta.model.enums.AccountTypeEnum;
import com.meta.model.enums.ShareSourceTypeEnum;
import com.meta.model.enums.SourceTypeEnum;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;

import java.sql.Timestamp;


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
    private SourceTypeEnum sourceType;

    // 首次阅读时间
    @TableField(value = "first_time")
    private Timestamp firstTime;

    // 最后阅读时间
    @TableField(value = "last_time")
    private Timestamp lastTime;

    // 阅读时长 (单位：秒)
    @TableField(value = "read_time")
    private Long readTime;

    // 当前阅读页码
    @TableField(value = "page_index")
    private Long pageIndex;

    // 当前阅读内容
    @TableField(value = "context_index")
    private String contextIndex;

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
