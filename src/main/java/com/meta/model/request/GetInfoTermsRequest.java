package com.meta.model.request;

import com.meta.model.enums.InfoTermsTypeEnum;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;

@Data
@Builder
public class GetInfoTermsRequest {

    private InfoTermsTypeEnum type;

    @Tolerate
    public GetInfoTermsRequest(){}
}
