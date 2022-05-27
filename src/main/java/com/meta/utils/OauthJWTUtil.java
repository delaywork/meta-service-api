package com.meta.utils;

import com.meta.model.*;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.nio.charset.StandardCharsets;

/**
 * @Author Martin
 */
@Log4j2
@Component
public class OauthJWTUtil {

    @Value("${JWT_SIGNING_KEY:}")
    private String jwt_signing_key;

    public MetaClaims getClaims(String token){
        log.info("进行 token 解析，token:{}",token);
        if (StringUtils.isBlank(token)){
            log.info("token 解析失败，token 为空");
            return MetaClaims.builder().build();
        }
        token = token.replace(" ", "").substring(6);

        Claims claims = Jwts.parserBuilder().setSigningKey(jwt_signing_key.getBytes(StandardCharsets.UTF_8)).build().parseClaimsJws(token).getBody();
        Long accountId = claims.get("accountId", Long.class);
        String accountName = claims.get("accountName", String.class);
        return MetaClaims.builder().accountId(accountId).accountName(accountName).build();
    }

}
