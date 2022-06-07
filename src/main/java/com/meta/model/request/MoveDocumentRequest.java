package com.meta.model.request;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;


/**
 * @Author Martin
 */
@Data
@Builder
public class MoveDocumentRequest {

    private Long folderId;

    private Long targetFolderId;

    private Long accountId;

    @Tolerate
    public MoveDocumentRequest(){}

}
