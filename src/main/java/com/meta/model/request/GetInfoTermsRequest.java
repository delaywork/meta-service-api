package com.meta.model.request;

import com.meta.model.enums.InfoTypeEnum;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;

@Data
@Builder
public class GetInfoTermsRequest {

    private InfoTypeEnum type;

    @Tolerate
    public GetInfoTermsRequest(){}
}
