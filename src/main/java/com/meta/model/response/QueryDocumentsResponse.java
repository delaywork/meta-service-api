package com.meta.model.response;

import com.meta.model.Page;
import com.meta.model.pojo.Document;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;

import java.util.List;

@Data
@Builder
public class QueryDocumentsResponse extends Page {

    // 当前文件夹
    private Document currentDocument;

    // 文件、文件夹
    private List<Document> documents;

    @Tolerate
    public QueryDocumentsResponse(){}
}
