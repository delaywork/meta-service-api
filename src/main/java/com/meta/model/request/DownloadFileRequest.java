package com.meta.model.request;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;

@Data
@Builder
public class DownloadFileRequest {

    // 文件id
    private Long fileId;

    @Tolerate
    public DownloadFileRequest(){}
}
