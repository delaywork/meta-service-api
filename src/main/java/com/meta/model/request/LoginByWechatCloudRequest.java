package com.meta.model.request;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;

@Data
@Builder
public class LoginByWechatCloudRequest {

    private String requestData;

    @Tolerate
    public LoginByWechatCloudRequest(){}
}
