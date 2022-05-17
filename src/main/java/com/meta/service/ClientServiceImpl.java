package com.meta.service;

import org.checkerframework.checker.units.qual.A;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.stereotype.Service;
import sun.misc.BASE64Encoder;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
public class ClientServiceImpl implements ClientDetailsService {

    @Override
    public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
        BaseClientDetails clientDetails = new BaseClientDetails();
        // 客户端 id
        clientDetails.setClientId("martin");
        // 资源 ids
        List<String> resourceIds = new ArrayList<>();
        clientDetails.setResourceIds(resourceIds);
        // 授权范围
        List<String> scopes = new ArrayList<>();
        scopes.add("all");
        clientDetails.setScope(scopes);
        // 认证类型
        List<String> authorizedGrantTypes = new ArrayList<>();
        authorizedGrantTypes.add("password");
        authorizedGrantTypes.add("refresh_token");
        clientDetails.setAuthorizedGrantTypes(authorizedGrantTypes);
        // 权限（对应的角色）
//        List<GrantedAuthority> authorities = new ArrayList<>();
//        clientDetails.setAuthorities(authorities);
        // 客户端密钥
        clientDetails.setClientSecret("$2a$10$RMuFXGQ5AtH4wOvkUqyvuecpqUSeoxZYqilXzbz50dceRsga.WYiq");
        // access token 有效时间
        clientDetails.setAccessTokenValiditySeconds(1800);
        // refresh token 有效时间
        clientDetails.setRefreshTokenValiditySeconds(1800);
        // 回调 url
//        clientDetails.setRegisteredRedirectUri();

        return clientDetails;
    }

    public static void main(String[] args) {
        final BASE64Encoder encoder = new BASE64Encoder();
        String encode = encoder.encode("martin".getBytes(StandardCharsets.UTF_8));
        System.out.println(encode);
    }
}
