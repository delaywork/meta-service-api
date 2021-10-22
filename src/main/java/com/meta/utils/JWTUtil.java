package com.meta.utils;

import cn.hutool.extra.spring.SpringUtil;
import com.meta.model.ErrorEnum;
import com.meta.model.FastRunTimeException;
import com.meta.model.BiteClaims;
import com.meta.model.RandomKeyResponse;
import com.meta.model.TokenResponse;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

/**
 * @Author Martin
 */
@Log4j2
public class JWTUtil {

    static RedisUtil redisUtils = SpringUtil.getBean(RedisUtil.class);
    private final static String ACCOUNT_ID = "accountId";
    private final static String TENANT_ID = "tenantId";
    private final static String LOGIN_TIME = "loginTime";
    private final static String VALIDITY_TIME = "validityTime";
    private final static String TOKEN_TYPE = "tokenType";
    private final static String ACCESS_TOKEN_TYPE = "accessToken";
    private final static String REFRESH_TOKEN_TYPE = "refreshToken";
    // accessToken有效时间：6小时
    private final static Long ACCESS_TOKEN_VALIDITY_TIME = 1000L * 60L * 60L * 6;
    // refreshToken有效时间：14天
    private final static Long REFRESH_TOKEN_VALIDITY_TIME = 1000L * 60L * 60L * 24 * 14;

    private static String getAccessToken(Long accountId, Long tenantId) {
        long currentTimeMillis = System.currentTimeMillis();
        long validityTime = currentTimeMillis + ACCESS_TOKEN_VALIDITY_TIME;
        Map<String, Object> map = new HashMap();
        map.put(ACCOUNT_ID, accountId);
        map.put(TENANT_ID, tenantId);
        map.put(LOGIN_TIME, currentTimeMillis);
        map.put(VALIDITY_TIME, validityTime);
        map.put(TOKEN_TYPE, ACCESS_TOKEN_TYPE);
        RandomKeyResponse randomKey = JWTKeys.getRandomKey();
        String jws = randomKey.getMapKey() + Jwts.builder().setClaims(map).signWith(randomKey.getKey()).compact();
        return jws;
    }

    private static String getRefreshToken(Long accountId, Long tenantId) {
        long currentTimeMillis = System.currentTimeMillis();
        long validityTime = currentTimeMillis + REFRESH_TOKEN_VALIDITY_TIME;
        Map<String, Object> map = new HashMap();
        map.put(ACCOUNT_ID, accountId);
        map.put(TENANT_ID, tenantId);
        map.put(LOGIN_TIME, currentTimeMillis);
        map.put(VALIDITY_TIME, validityTime);
        map.put(TOKEN_TYPE, REFRESH_TOKEN_TYPE);
        RandomKeyResponse randomKey = JWTKeys.getRandomKey();
        String jws = randomKey.getMapKey() + Jwts.builder().setClaims(map).signWith(randomKey.getKey()).compact();
        return jws;
    }

    public static TokenResponse getAccessTokenAndRefreshToken(Long accountId, Long tenantId) {
        long currentTimeMillis = System.currentTimeMillis();
        String accessToken = getAccessToken(accountId, tenantId);
        String refreshToken = getRefreshToken(accountId, tenantId);
        return TokenResponse.builder().accessToken(accessToken).refreshToken(refreshToken).build();
    }

    /**
     * 根据refreshToken生成两个token
     */
    public static TokenResponse getAccessTokenAndRefreshTokenByRefreshToken(String refreshToken) {
        // 调用checkToken对refreshToken进行验证和解析
        BiteClaims biteClaims = checkToken(refreshToken);
        if (REFRESH_TOKEN_TYPE.equals(biteClaims.getTokenType())) {
            return getAccessTokenAndRefreshToken(biteClaims.getAccountId(), biteClaims.getTenantId());
        }
        throw new FastRunTimeException(ErrorEnum.Token过期或已失效);
    }

    /**
     * 验证并解析token
     */
    public static BiteClaims checkToken(String token) {
        if (StringUtils.isBlank(token)) {
            log.info("token 不存在");
            throw new FastRunTimeException(ErrorEnum.Token过期或已失效);
        }
        Claims claims = null;
        try {
            String mapKey = token.substring(0, 1);
            String jws = token.substring(1, token.length());
            claims = Jwts.parserBuilder().setSigningKey(JWTKeys.getKey(mapKey)).build().parseClaimsJws(jws).getBody();
            Long validityTime = claims.get(VALIDITY_TIME, Long.class);
            if (System.currentTimeMillis() > validityTime) {
                log.info("token 已过期");
                throw new FastRunTimeException(ErrorEnum.Token过期或已失效);
            }
        } catch (Exception e) {
            log.info("token 解析错误");
            throw new FastRunTimeException(ErrorEnum.Token过期或已失效);
        }
        return BiteClaims.builder().accountId(claims.get(ACCOUNT_ID, Long.class))
                .times(claims.get(VALIDITY_TIME, Long.class))
                .tokenType(claims.get(TOKEN_TYPE, String.class))
                .loginTime(claims.get(LOGIN_TIME, Long.class))
                .tenantId(claims.get(TENANT_ID, Long.class)).build();
    }

    /**
     * 解析token
     */
    public static BiteClaims getBiteClaims(String token) {
        if (StringUtils.isBlank(token)) {
            throw new FastRunTimeException(ErrorEnum.Token过期或已失效);
        }
        Claims claims = null;
        try {
            String mapKey = token.substring(0, 1);
            String jws = token.substring(1, token.length());
            claims = Jwts.parserBuilder().setSigningKey(JWTKeys.getKey(mapKey)).build().parseClaimsJws(jws).getBody();
            Long validityTime = claims.get(VALIDITY_TIME, Long.class);
            if (System.currentTimeMillis() > validityTime) {
                throw new FastRunTimeException(ErrorEnum.Token过期或已失效);
            }
        } catch (Exception e) {
            throw new FastRunTimeException(ErrorEnum.Token过期或已失效);
        }
        return BiteClaims.builder().accountId(claims.get(ACCOUNT_ID, Long.class))
                .times(claims.get(VALIDITY_TIME, Long.class))
                .tokenType(claims.get(TOKEN_TYPE, String.class))
                .loginTime(claims.get(LOGIN_TIME, Long.class))
                .tenantId(claims.get(TENANT_ID, Long.class)).build();
    }

}
