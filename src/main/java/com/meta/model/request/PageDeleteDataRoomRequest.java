package com.meta.model.request;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.meta.model.pojo.DataRoom;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;


/**
 * @Author Martin
 */
@Data
@Builder
public class PageDeleteDataRoomRequest {

    private Page<DataRoom> page;

    @Tolerate
    public PageDeleteDataRoomRequest(){}

}
