package com.meta.model.response;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;

import java.util.List;


@Data
@Builder
public class GetInfoResponse {

    // 关于
    private List<String> abouts;

    // 条款
    private List<String> termsAndConditions;

    // 策略
    private List<String> policies;

    @Tolerate
    public GetInfoResponse(){}
}
