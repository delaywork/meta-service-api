package com.meta.model;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import java.util.LinkedHashMap;
import java.util.Map;

public class MyJwt extends JwtAccessTokenConverter {

    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        Map<String, Object> additionalInformation = new LinkedHashMap<>();
        Map<String, Object> info = new LinkedHashMap<>();
        info.put("author", "zhengqingya");
        info.put("gitee", "https://gitee.com/zhengqingya");
        info.put("user", SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        additionalInformation.put("info", info);
        ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInformation);
        return super.enhance(accessToken, authentication);
    }

}
