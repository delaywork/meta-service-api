package com.meta.model.request;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;


/**
 * @Author Martin
 */
@Data
@Builder
public class UpdateFolderNameRequest {

    private Long folderId;

    private String name;

    @Tolerate
    public UpdateFolderNameRequest(){}

}
