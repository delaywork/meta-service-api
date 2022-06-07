package com.meta.model.request;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;
import org.springframework.web.multipart.MultipartFile;


/**
 * @Author Martin
 */
@Data
@Builder
public class UpdateFileRequest {

    private Long documentId;

    private MultipartFile file;

    private String name;

    private Long parentId;

    private String comments;

    // 操作人id
    private Long accountId;

    @Tolerate
    public UpdateFileRequest(){}

}
