//package com.meta.configure;
//
//import com.meta.model.request.vo.UserOauthVo;
//import lombok.extern.log4j.Log4j2;
//import org.springframework.security.authentication.AuthenticationProvider;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.stereotype.Component;
//
//import java.util.ArrayList;
//
//@Log4j2
//@Component
//public class WeChatAuthenticationProvider implements AuthenticationProvider {
//
//
//    @Override
//    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
//        log.info("WeChatAuthenticationProvider ---> authenticate ---> start");
//        WeChatAuthenticationToken authToken = (WeChatAuthenticationToken) authentication;
//        //调用userdetailService获取认证信息（按自己的业务实现）返回封装好的SysAuthUser
//        UserOauthVo userOauthVo = new UserOauthVo(1l, "admin", "$2a$10$yJSqqr6sTxNuYtA6EKcVUe2I4USFCzJ29sNcRrBvtAkSYcNg5ydQ6", true, new ArrayList<>());
//        //认证成功后构造一个新的AuthenticationToken，传入认证好的用户信息和权限信息等
//        WeChatAuthenticationToken authenticationResult = new WeChatAuthenticationToken(userOauthVo, userOauthVo.getPassword(), userOauthVo.getAuthorities());
//        authenticationResult.setDetails(authToken.getDetails());
//        log.info("WeChatAuthenticationProvider ---> authenticate ---> end");
//        return authenticationResult;
//    }
//
//    @Override
//    public boolean supports(Class<?> authentication) {
//        return WeChatAuthenticationToken.class.isAssignableFrom(authentication);
//    }
//}
