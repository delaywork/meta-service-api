package com.meta.configure;

import com.meta.model.request.vo.UserOauthVo;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.token.AbstractTokenGranter;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

@Log4j2
public class WeChatTokenGranter extends AbstractTokenGranter {

    public WeChatTokenGranter(AuthorizationServerTokenServices tokenServices, ClientDetailsService clientDetailsService, OAuth2RequestFactory requestFactory, String grantType) {
        super(tokenServices, clientDetailsService, requestFactory, grantType);
    }

    @Override
    protected OAuth2Authentication getOAuth2Authentication(ClientDetails client, TokenRequest tokenRequest) {
        log.info("WeChatTokenGranter ---> getOAuth2Authentication ---> start");
        // return super.getOAuth2Authentication(client, tokenRequest);
        LinkedHashMap<String, String> parameters  = new LinkedHashMap<>(tokenRequest.getRequestParameters());
        // 可以获取到传入的参数
        String userMobileNo = parameters.get("mobile");  //客户端提交的用户名
        String smsCode = parameters.get("smscode");  //客户端提交的验证码

        // TODO 写自己的验证逻辑
        List<SimpleGrantedAuthority> roles = new ArrayList<>();
        roles.add(new SimpleGrantedAuthority("admin"));
        UserOauthVo user = new UserOauthVo(1l, "admin", "$2a$10$RMuFXGQ5AtH4wOvkUqyvuecpqUSeoxZYqilXzbz50dceRsga.WYiq", true, roles);

        Authentication userAuth = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        ((AbstractAuthenticationToken) userAuth).setDetails(parameters);

        OAuth2Request storedOAuth2Request = getRequestFactory().createOAuth2Request(client, tokenRequest);
        log.info("WeChatTokenGranter ---> getOAuth2Authentication ---> end");
        return new OAuth2Authentication(storedOAuth2Request, userAuth);
    }
}
