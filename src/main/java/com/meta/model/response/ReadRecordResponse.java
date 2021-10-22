package com.meta.model.response;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.meta.model.enums.AccountTypeEnum;
import com.meta.model.enums.DataRoomTypeEnum;
import com.meta.model.enums.ShareSourceTypeEnum;
import com.meta.model.pojo.ReadTime;
import com.meta.model.response.vo.ReadTimeVO;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;

import java.util.List;

@Data
@Builder
public class ReadRecordResponse {

    // 主键ID
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long ReadScheduleId;

    // 阅读源 id
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long sourceId;

    // 阅读源类型
    private ShareSourceTypeEnum sourceType;

    // 首次阅读时间
    private Long firstTime;

    // 最后阅读时间
    private Long lastTime;

    // 阅读时长
    private Long readTime;

    // 当前阅读页
    private Integer currentPage;

    // 阅读人员 id
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long accountId;

    // 阅读人员类型
    private AccountTypeEnum accountType;

    // 每页阅读时长
    private JSONObject pageReadTime;

    // 阅读次数
    private List<ReadTimeVO> readTimeList;

    @Tolerate
    public ReadRecordResponse(){}
}
