package com.meta.model.request;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;


/**
 * @Author Martin
 */
@Data
@Builder
public class MoveDataRoomRequest {

    private Long folderId;
    private Long targetFolderId;

    @Tolerate
    public MoveDataRoomRequest(){}

}
