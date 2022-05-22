package com.meta.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.endpoint.TokenEndpoint;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Api(tags = "认证中心")
@RestController
@RequestMapping("/oauth")
@Slf4j
public class OauthController {

    @Resource
    private TokenEndpoint tokenEndpoint;

//    @ApiOperation(value = "OAuth2认证", notes = "login")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "grant_type", defaultValue = "password", value = "授权模式", required = true),
//            @ApiImplicitParam(name = "client_id", value = "Oauth2客户端ID（新版本需放置请求头）", required = true),
//            @ApiImplicitParam(name = "client_secret", value = "Oauth2客户端秘钥（新版本需放置请求头）", required = true),
//            @ApiImplicitParam(name = "refresh_token", value = "刷新token"),
//            @ApiImplicitParam(name = "username", defaultValue = "admin", value = "登录用户名"),
//            @ApiImplicitParam(name = "password", defaultValue = "123456", value = "登录密码"),
//            @ApiImplicitParam(name = "phone", defaultValue = "13888888888", value = "手机号"),
//            @ApiImplicitParam(name = "smsCode", defaultValue = "000000", value = "验证码")
//    })
//    @PostMapping("/token")
//    public Object postAccessToken(
//            @ApiIgnore Principal principal,
//            @ApiIgnore @RequestParam Map<String, String> parameters
//    ) throws HttpRequestMethodNotSupportedException {
//        return tokenEndpoint.postAccessToken(principal, parameters).getBody();
//    }
//
    @GetMapping("/login")
    public Object postAccessToken() throws HttpRequestMethodNotSupportedException {
        //创建客户端信息,客户端信息可以写死进行处理，因为Oauth2密码模式，客户端双信息必须存在，所以伪装一个
        //如果不想这么用，需要重写比较多的代码
        //这里设定，调用这个接口的都是资源服务
        User clientUser = new User("martin", "123", new ArrayList<>());
        //生成已经认证的client
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(clientUser, null, new ArrayList<>());

        Map<String, String> parameters = new HashMap<>();
        parameters.put("grant_type","wechat");
//        parameters.put("grant_type","password");
        parameters.put("client_id","martin");
        parameters.put("client_secret","123");
        parameters.put("smscode","123456");
        parameters.put("mobile","admin");
//        parameters.put("username","admin");
//        parameters.put("password","123");
        parameters.put("scope","all");
        return tokenEndpoint.postAccessToken(token, parameters).getBody();
    }

}

