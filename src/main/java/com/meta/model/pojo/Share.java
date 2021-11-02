package com.meta.model.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.meta.model.BaseModel;
import com.meta.model.enums.DataRoomTypeEnum;
import com.meta.model.enums.ShareSourceTypeEnum;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;


/**
 * @Author Martin
 */
@Data
@Builder
@TableName(value="share")
public class Share extends BaseModel {

    // 主键ID 使用雪花算法生成
    @TableId(value = "id",type = IdType.NONE)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;

    // 分享源 id
    @TableField(value = "source_id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long sourceId;

    // 分享源类型
    @TableField(value = "source_type")
    private ShareSourceTypeEnum sourceType;

    // 名称
    @TableField(value = "name")
    private String name;

    // 描述
    @TableField(value = "note")
    private String note;

    // 允许访问
    @TableField(value = "allow_access")
    private Boolean allowAccess;

    // 允许分享
    @TableField(value = "allow_share")
    private Boolean allowShare;

    // 是否需要水印
    @TableField(value = "have_watermark")
    private Boolean haveWatermark = true;

    // 水印类容
    @TableField(value = "watermark_text")
    private String watermarkText;

    // 失效时间
    @TableField(value = "expire_times")
    private Long expireTimes = null;

    // 密码
    @TableField(value = "password")
    private String password = null;

    // 访问次数
    @TableField(value = "read_time")
    private Integer readTime = null;

    // 水印文件地址
    @TableField(value = "watermark_url")
    private String watermarkUrl;

    // 源文件地址
    @TableField(value = "source_url")
    private String sourceUrl;

    // 分享人
    @TableField(value = "share_account_id")
    private Long shareAccountId;

    // 租户id
    @TableField(value = "tenant_id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long tenantId;

    @Tolerate
    public Share(){}

}
