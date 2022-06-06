package com.meta.model.request;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;


/**
 * @Author Martin
 */
@Data
@Builder
public class AddFolderRequest {

    // 父节点
    private Long parentId;

    // 名称
    private String name;

    // 描述
    private String comments;

    // 操作人
    private Long accountId;

    @Tolerate
    public AddFolderRequest(){}

}
