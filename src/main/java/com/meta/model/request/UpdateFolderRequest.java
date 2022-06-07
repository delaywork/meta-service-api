package com.meta.model.request;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;


/**
 * @Author Martin
 */
@Data
@Builder
public class UpdateFolderRequest {

    private Long folderId;

    private String name;

    private Long parentId;

    private String comments;

    // 操作人id
    private Long accountId;

    @Tolerate
    public UpdateFolderRequest(){}

}
