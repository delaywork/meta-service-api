package com.meta.model.response.vo;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.meta.model.BaseModel;
import com.meta.model.enums.AccountTypeEnum;
import com.meta.model.enums.DataRoomTypeEnum;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;


/**
 * @Author Martin
 */
@Data
@Builder
public class ReadTimeVO {

    // 主键ID
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;

    // 开始阅读时间
    private Long startTime;

    // 阅读时长
    private Long readTime;

    // 主要阅读页码
    private JSONObject readContent;

    @Tolerate
    public ReadTimeVO(){}

}
