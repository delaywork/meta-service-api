package com.meta.controller;

import com.meta.model.FastRunTimeException;
import com.meta.model.ReturnData;
import com.meta.model.enums.OauthGrantTypeEnum;
import com.meta.model.request.LoginByWechatRequest;
import com.meta.utils.RSAUtil;
import com.meta.utils.WechatUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
@Slf4j
public class OauthController {

    @Value("${META_CLIENT_ID:}")
    private String meta_client_id;
    @Value("${META_CLIENT_SECRET:}")
    private String meta_client_secret;

    @Resource
    private TokenEndpoint tokenEndpoint;

    @GetMapping("/token")
    public ReturnData token(@RequestParam("authType") OauthGrantTypeEnum authType, @RequestParam("jsCode") String jsCode) throws HttpRequestMethodNotSupportedException {
        // 构造一个被认证的客户端
        User clientUser = new User(meta_client_id, meta_client_secret, new ArrayList<>());
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(clientUser, null, new ArrayList<>());
        // 认证参数
        Map<String, String> parameters = new HashMap<>();
        parameters.put("grant_type", authType.getOauthValue());
        parameters.put("client_id",meta_client_id);
        parameters.put("client_secret",meta_client_secret);
        parameters.put("scope","all");
        parameters.put("jsCode",jsCode);
        try{
            OAuth2AccessToken oAuth2AccessToken = tokenEndpoint.postAccessToken(token, parameters).getBody();
            return ReturnData.success(oAuth2AccessToken);
        }catch (FastRunTimeException fastRunTimeException){
            return ReturnData.failed(fastRunTimeException);
        }
    }

    public static void main(String[] args) {
        String pass = "meta";
        BCryptPasswordEncoder bcryptPasswordEncoder = new BCryptPasswordEncoder();
        String hashPass = bcryptPasswordEncoder.encode(pass);
        System.out.println(hashPass);

        boolean f = bcryptPasswordEncoder.matches("123","$2a$10$RMuFXGQ5AtH4wOvkUqyvuecpqUSeoxZYqilXzbz50dceRsga.WYiq");
        System.out.println(f);


    }

}

