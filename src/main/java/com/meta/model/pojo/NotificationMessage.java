package com.meta.model.pojo;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;

import java.util.Date;

@Data
@Builder
public class NotificationMessage {
    public Long from;
    /**
     * 发送者名称
     */
    public String fromName;
    /**
     * 接收者账号
     */
    public Long to;
    /**
     * 发送的内容
     */
    public String text;
    /**
     * 发送的日期
     */
    public Date date;

    @Tolerate
    public NotificationMessage(){};

}
