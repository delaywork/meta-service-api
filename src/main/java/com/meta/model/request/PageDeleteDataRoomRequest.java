package com.meta.model.request;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.meta.model.pojo.Document;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;


/**
 * @Author Martin
 */
@Data
@Builder
public class PageDeleteDataRoomRequest {

    private Page<Document> page;

    @Tolerate
    public PageDeleteDataRoomRequest(){}

}
