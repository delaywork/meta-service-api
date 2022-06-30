package com.meta.model.pojo;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;

@Data
@Builder
public class ReadTimeDetail {

    // 页码
    private Long pageIndex;

    // 阅读时长 (单位：秒)
    private Long readTime;

    @Tolerate
    public ReadTimeDetail(){}
}
