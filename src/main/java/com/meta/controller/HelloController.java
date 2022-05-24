package com.meta.controller;

import com.meta.model.MetaClaims;
import com.meta.utils.OauthJWTUtil;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class HelloController {

    @Value("${JWT_SIGNING_KEY:}")
    private String jwt_signing_key;

    @Resource
    private OauthJWTUtil oauthJWTUtil;

    @GetMapping("/admin/hello")
    public String admin() {
        return "hello admin";
    }

    @GetMapping("/user/hello")
    public String user(@RequestHeader("authorization") String token){
        MetaClaims claims = oauthJWTUtil.getClaims(token);
        return "hello user";
    }

    @GetMapping("/hello")
    public String hello() {
        return "hello";
    }

}
