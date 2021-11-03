package com.meta.model.request;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.meta.model.pojo.DataRoom;
import com.meta.model.pojo.ReadSchedule;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;


/**
 * @Author Martin
 */
@Data
@Builder
public class PageReadRecordByAccountRequest {

    private Page<ReadSchedule> page;

    @Tolerate
    public PageReadRecordByAccountRequest(){}

}
