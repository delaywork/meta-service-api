package com.meta.model.request;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;


/**
 * @Author Martin
 */
@Data
@Builder
public class AddFolderDataRoomRequest {

    // 父节点（跟节点不存在父节点）
    private Long parentId;

    // 名称
    private String name;

    // 描述
    private String comments;

    @Tolerate
    public AddFolderDataRoomRequest(){}

}
