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
public class AddDocumentRequest {

    private MultipartFile document;

    // 父节点
    private Long parentId;

    // 名称
    private String name;

    // 描述
    private String comments;

    // 操作人
    private Long accountId;

    @Tolerate
    public AddDocumentRequest(){}

}
