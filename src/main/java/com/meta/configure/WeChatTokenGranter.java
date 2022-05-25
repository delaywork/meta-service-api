package com.meta.configure;

import cn.hutool.extra.spring.SpringUtil;
import com.meta.model.ErrorEnum;
import com.meta.model.FastRunTimeException;
import com.meta.model.WechatUtilLoginResponse;
import com.meta.model.enums.AuthorityEnum;
import com.meta.model.pojo.Account;
import com.meta.model.request.AddAccountRequest;
import com.meta.model.request.GetAccountRequest;
import com.meta.model.request.vo.UserOauthVo;
import com.meta.service.AccountServiceImpl;
import com.meta.utils.RedisUtil;
import com.meta.utils.WechatUtil;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.token.AbstractTokenGranter;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

@Log4j2
public class WeChatTokenGranter extends AbstractTokenGranter {

    @Resource
    private WechatUtil wechatUtil;
    @Resource
    private AccountServiceImpl accountService = SpringUtil.getBean(AccountServiceImpl.class);

    public WeChatTokenGranter(AuthorizationServerTokenServices tokenServices, ClientDetailsService clientDetailsService, OAuth2RequestFactory requestFactory, String grantType) {
        super(tokenServices, clientDetailsService, requestFactory, grantType);
    }

    @Override
    protected OAuth2Authentication getOAuth2Authentication(ClientDetails client, TokenRequest tokenRequest) {
        log.info("WeChatTokenGranter ---> getOAuth2Authentication ---> start");
        LinkedHashMap<String, String> parameters  = new LinkedHashMap<>(tokenRequest.getRequestParameters());
        // 微信认证
        String jsCode = parameters.get("jsCode");
        String userName = parameters.get("userName");
        String avatarUrl = parameters.get("avatarUrl");
        log.info("微信认证 jsCode:{}", jsCode);
        WechatUtilLoginResponse wechatResponse = wechatUtil.login(jsCode);
//        WechatUtilLoginResponse wechatResponse = new WechatUtilLoginResponse();
//        wechatResponse.setOpenid("2");
//        wechatResponse.setUnionid("2");
        if (ObjectUtils.isEmpty(wechatResponse)){
            log.info("微信认证失败 jsCode:{}", jsCode);
            throw new FastRunTimeException(ErrorEnum.认证异常);
        }
        // 查询账户
        Account account = accountService.getAccount(GetAccountRequest.builder().openid(wechatResponse.getOpenid()).unionid(wechatResponse.getUnionid()).build());
        if (ObjectUtils.isEmpty(account)){
            // 创建账户
            log.info("创建微信关联账号，openId:{}, unionId:{}", wechatResponse.getOpenid(), wechatResponse.getUnionid());
            account = accountService.addAccount(AddAccountRequest.builder().openid(wechatResponse.getOpenid()).unionid(wechatResponse.getUnionid()).name(userName).avatarUrl(avatarUrl).build());
        }
        // 生成认证账号信息
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        if (ObjectUtils.isNotEmpty(account.getAreaCode()) && ObjectUtils.isNotEmpty(account.getPhone())){
            log.info("微信关联账号已经完成手机因子绑定，accountId:{}, areaCode:{}, phone:{}", account.getId(), account.getAreaCode(), account.getPhone());
            authorities.add(new SimpleGrantedAuthority(AuthorityEnum.ALL.getValue()));
        }else{
            log.info("微信关联账号未完成手机因子绑定，accountId:{}", account.getId());
            authorities.add(new SimpleGrantedAuthority(AuthorityEnum.INBOX.getValue()));
        }
        UserOauthVo user = new UserOauthVo(account.getId(), account.getName(), account.getPassword(), true, authorities);

        Authentication userAuth = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        ((AbstractAuthenticationToken) userAuth).setDetails(parameters);

        OAuth2Request storedOAuth2Request = getRequestFactory().createOAuth2Request(client, tokenRequest);
        log.info("WeChatTokenGranter ---> getOAuth2Authentication ---> end");
        return new OAuth2Authentication(storedOAuth2Request, userAuth);
    }
}
