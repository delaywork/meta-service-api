package com.meta.model.request;

import com.meta.model.enums.MetaTermsTypeEnum;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;

@Data
@Builder
public class GetMetaTermsRequest {

    private MetaTermsTypeEnum type;

    @Tolerate
    public GetMetaTermsRequest(){}
}
