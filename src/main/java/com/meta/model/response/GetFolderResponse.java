package com.meta.model.response;

import com.meta.model.pojo.Document;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;

import java.util.List;

@Data
@Builder
public class GetFolderResponse {

    // 文件、文件夹
    private List<Document> documentList;

    @Tolerate
    public GetFolderResponse(){}
}
