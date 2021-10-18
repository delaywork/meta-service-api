package com.meta.utils.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author Martin
 * @Date 2021/8/9 09:41
 * @remark
 */
@Data
@Builder
public class BiteClaims implements Serializable {

    private static final long serialVersionUID = -674402862690602444L;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long accountId;

    private Long times;

    private String tokenType;

    private Long loginTime;


}
