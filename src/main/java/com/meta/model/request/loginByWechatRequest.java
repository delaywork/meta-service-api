package com.meta.model.request;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;


/**
 * @Author Martin
 */
@Data
@Builder
public class loginByWechatRequest {

    private String jsCode;

    @Tolerate
    public loginByWechatRequest(){}

}
