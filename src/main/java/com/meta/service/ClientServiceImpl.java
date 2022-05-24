package com.meta.service;

import com.meta.model.enums.OauthGrantTypeEnum;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class ClientServiceImpl implements ClientDetailsService {

    @Value("${META_CLIENT_ID:}")
    private String META_CLIENT_ID;
    @Value("${META_CLIENT_SECRET:}")
    private String META_CLIENT_SECRET;
    @Value("${ACCESS_TOKEN_VALIDITY_SECONDS:}")
    private Integer ACCESS_TOKEN_VALIDITY_SECONDS;
    @Value("${REFRESH_TOKEN_VALIDITY_SECONDS:}")
    private Integer REFRESH_TOKEN_VALIDITY_SECONDS;

    @Override
    public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
        BaseClientDetails clientDetails = new BaseClientDetails();
        // 客户端 id
        clientDetails.setClientId(META_CLIENT_ID);
        // 客户端密钥
        clientDetails.setClientSecret(META_CLIENT_SECRET);
        // 资源 ids
        List<String> resourceIds = new ArrayList<>();
        clientDetails.setResourceIds(resourceIds);
        // 授权范围
        List<String> scopes = new ArrayList<>();
        scopes.add("all");
        clientDetails.setScope(scopes);
        // 认证类型
        List<String> authorizedGrantTypes = new ArrayList<>();
        authorizedGrantTypes.add(OauthGrantTypeEnum.PASSWORD.getOauthValue());
        authorizedGrantTypes.add(OauthGrantTypeEnum.REFRESH_TOKEN.getOauthValue());
        authorizedGrantTypes.add(OauthGrantTypeEnum.WECHAT.getOauthValue());
        clientDetails.setAuthorizedGrantTypes(authorizedGrantTypes);
        // 权限（对应的角色）
//        List<GrantedAuthority> authorities = new ArrayList<>();
//        clientDetails.setAuthorities(authorities);
        // access token 有效时间
        clientDetails.setAccessTokenValiditySeconds(ACCESS_TOKEN_VALIDITY_SECONDS);
        // refresh token 有效时间
        clientDetails.setRefreshTokenValiditySeconds(REFRESH_TOKEN_VALIDITY_SECONDS);
        // 回调 url
//        clientDetails.setRegisteredRedirectUri();

        return clientDetails;
    }

}
