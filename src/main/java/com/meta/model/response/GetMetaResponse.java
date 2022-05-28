package com.meta.model.response;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;


@Data
@Builder
public class GetMetaResponse {

    // 关于
    private String about;

    // 条款
    private String termsAndConditions;

    // 策略
    private String policy;

    @Tolerate
    public GetMetaResponse(){}
}
