package com.meta.model.request;

import com.meta.model.enums.AccountTypeEnum;
import com.meta.model.enums.ShareSourceTypeEnum;
import com.meta.model.enums.SourceTypeEnum;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;

import java.sql.Timestamp;

@Data
@Builder
public class UpdateReadRecordRequest {

    // 阅读源 id
    private Long sourceId;

    // 阅读源类型
    private SourceTypeEnum sourceType;

    // 开始阅读时间
    private Timestamp startTime;

    // 阅读时长
    private Long times;

    // 当前阅读页码
    private Long pageIndex;

    // 关联的长链接id
    private String webSocketSessionId;

    // 阅读人员 id
    private Long accountId;

    // 阅读人员类型
    private AccountTypeEnum accountType;

    @Tolerate
    public UpdateReadRecordRequest(){}
}
