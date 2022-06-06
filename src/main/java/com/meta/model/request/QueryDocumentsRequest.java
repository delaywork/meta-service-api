package com.meta.model.request;

import com.meta.model.Order;
import com.meta.model.Page;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;

import java.util.List;

@Data
@Builder
public class QueryDocumentsRequest extends Page {

    private List<String> names;

    private List<Order> orders;

    private Long documentId;

    private Long accountId;

    private Long tenantId;

    @Tolerate
    public QueryDocumentsRequest(){}
}
