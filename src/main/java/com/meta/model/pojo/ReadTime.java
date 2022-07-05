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
import com.meta.model.pojo.handler.ReadTimeDetailJsonArrayHandler;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;

import java.sql.Timestamp;
import java.util.List;


/**
 * @Author Martin
 * 阅读次数
 */
@Data
@Builder
@TableName(value="read_times", autoResultMap = true)
public class ReadTime extends BaseModel {

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

    // 开始阅读时间
    @TableField(value = "start_time")
    private Timestamp startTime;

    // 阅读时长 (单位：秒)
    @TableField(value = "read_time")
    private Long readTime;

    // 主要阅读页码 (使用 json 序列化形式存储，json: {页码：时长 (单位：秒)})
    @TableField(value = "detail", typeHandler = ReadTimeDetailJsonArrayHandler.class)
    private List<ReadTimeDetail> detail;

    // 关联的长链接 session id
    @TableField(value = "web_socket_session_id")
    private String webSocketSessionId;

    // 阅读人员 id
    @TableField(value = "account_id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long accountId;

    // 阅读人员类型
    @TableField(value = "account_type")
    private AccountTypeEnum accountType;

    @Tolerate
    public ReadTime(){}

}
