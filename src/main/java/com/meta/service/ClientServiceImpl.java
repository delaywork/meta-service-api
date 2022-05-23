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
    private String meta_client_id;
    @Value("${META_CLIENT_SECRET:}")
    private String meta_client_secret;

    @Override
    public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
        BaseClientDetails clientDetails = new BaseClientDetails();
        // 客户端 id
        clientDetails.setClientId(meta_client_id);
        // 客户端密钥
        clientDetails.setClientSecret(meta_client_secret);
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
        clientDetails.setAccessTokenValiditySeconds(1800);
        // refresh token 有效时间
        clientDetails.setRefreshTokenValiditySeconds(1800);
        // 回调 url
//        clientDetails.setRegisteredRedirectUri();

        return clientDetails;
    }

}
