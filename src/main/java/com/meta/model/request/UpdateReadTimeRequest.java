package com.meta.model.request;

import com.meta.model.enums.AccountTypeEnum;
import com.meta.model.enums.DataRoomTypeEnum;
import com.meta.model.enums.ShareSourceTypeEnum;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;

@Data
@Builder
public class UpdateReadTimeRequest {

    // 主键ID
    private Long id;

    // 阅读源 id
    private Long sourceId;

    // 阅读源类型
    private ShareSourceTypeEnum sourceType;

    private String readContent;

    // 开始阅读时间
    private Long startTime;

    // 当前阅读页码
    private Integer currentPage;

    // 阅读人员 id
    private Long accountId;

    // 阅读人员类型
    private AccountTypeEnum accountType;

    @Tolerate
    public UpdateReadTimeRequest(){}
}
