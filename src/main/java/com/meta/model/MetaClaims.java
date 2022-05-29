package com.meta.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author Martin
 */
@Data
@Builder
public class MetaClaims implements Serializable {

    private static final long serialVersionUID = -674402862690602444L;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long accountId;

    private String accountName;


}
