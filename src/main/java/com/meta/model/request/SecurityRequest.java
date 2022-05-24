package com.meta.model.request;

import com.meta.model.enums.VerificationMethodEnum;
import com.meta.model.enums.VerificationScenarioEnum;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;

@Data
@Builder
public class SecurityRequest {

    private VerificationMethodEnum method;

    private String areaCode;

    private String phone;

    private String email;

    private String code;

    private Long accountId;

    @Tolerate
    public SecurityRequest(){}
}
