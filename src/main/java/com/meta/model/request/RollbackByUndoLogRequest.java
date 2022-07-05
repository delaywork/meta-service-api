package com.meta.model.request;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;

@Data
@Builder
public class RollbackByUndoLogRequest {

    private Long undoLogId;

    private Long accountId;

    @Tolerate
    public RollbackByUndoLogRequest(){}
}
