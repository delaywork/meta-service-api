package com.meta.model.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;


@Data
@Builder
public class UpdateFileResponse {

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long documentUndoLogId;

    @Tolerate
    public UpdateFileResponse(){}
}
