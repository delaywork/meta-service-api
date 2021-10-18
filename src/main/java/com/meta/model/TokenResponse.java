package com.meta.model;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author Martin
 */
@Data
@Builder
public class TokenResponse implements Serializable {

    private static final long serialVersionUID = -935438290887411032L;

    private String accessToken;

    private String refreshToken;
}
