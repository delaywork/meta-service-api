package com.meta.model.response.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.meta.model.enums.AccountTypeEnum;
import com.meta.model.enums.ShareSourceTypeEnum;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;

/**
 * @Author Martin
 */
@Data
@Builder
public class ReadRecordBySourceVO {

    // readScheduleId
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long readScheduleId;

    // 阅读源 id
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long sourceId;

    // 阅读源类型
    private ShareSourceTypeEnum sourceType;

    // 首次阅读时间
    private Long firstTime;

    // 最后阅读时间
    private Long lastTime;

    // 阅读时长 (单位：秒)
    private Long readTime;

    // 当前阅读页
    private Integer currentPage;

    // 阅读人员 id
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long accountId;

    // 阅读人员类型
    private AccountTypeEnum accountType;

    @Tolerate
    public ReadRecordBySourceVO(){}

}
