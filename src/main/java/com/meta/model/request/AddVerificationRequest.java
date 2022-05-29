package com.meta.model.request;

import com.meta.model.enums.VerificationMethodEnum;
import com.meta.model.enums.VerificationScenarioEnum;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;

@Data
@Builder
public class AddVerificationRequest {

    private VerificationMethodEnum method;

    private VerificationScenarioEnum scenario;

    private String areaCode;

    private String phone;

    private String email;

    @Tolerate
    public AddVerificationRequest(){}
}
