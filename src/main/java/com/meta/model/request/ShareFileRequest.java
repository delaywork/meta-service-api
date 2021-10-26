package com.meta.model.request;

import com.meta.model.enums.ShareSourceTypeEnum;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;


/**
 * @Author Martin
 */
@Data
@Builder
public class ShareFileRequest {

    // 分享源id
    private Long sourceId;

    // 分享源类型
    private ShareSourceTypeEnum sourceType;

    @Tolerate
    public ShareFileRequest(){}

}
