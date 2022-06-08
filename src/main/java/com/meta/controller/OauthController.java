package com.meta.controller;

import com.meta.model.ErrorEnum;
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
    public ReturnData token(@RequestParam(value = "refreshToken", required = false) String refreshToken, @RequestParam("authType") OauthGrantTypeEnum authType, @RequestParam(value = "jsCode", required = false) String jsCode,
                            @RequestParam(value = "userName", required = false) String userName, @RequestParam(value = "avatarUrl", required = false) String avatarUrl,
                            @RequestParam(value = "password", required = false) String password) throws HttpRequestMethodNotSupportedException {
        log.info("获取 token，authType:{}", authType.getOauthValue());
        // 构造一个被认证的客户端
        User clientUser = new User(meta_client_id, meta_client_secret, new ArrayList<>());
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(clientUser, null, new ArrayList<>());
        // 认证参数
        Map<String, String> parameters = new HashMap<>();
        parameters.put("grant_type", authType.getOauthValue());
        parameters.put("client_id",meta_client_id);
        parameters.put("client_secret",meta_client_secret);
        parameters.put("scope","all");
        parameters.put("userName",userName);
        parameters.put("avatarUrl",avatarUrl);
        parameters.put("refresh_token",refreshToken);
        parameters.put("password",password);
        parameters.put("jsCode",jsCode);
        try{
            OAuth2AccessToken oAuth2AccessToken = tokenEndpoint.postAccessToken(token, parameters).getBody();
            return ReturnData.success(oAuth2AccessToken);
        }catch (FastRunTimeException fastRunTimeException){
            return ReturnData.failed(fastRunTimeException);
        }catch (Exception exception){
            switch (authType){
                case REFRESH_TOKEN:
                    log.info("refresh_token:{}", refreshToken);
            }
            log.info("认证失败，message:{}", exception.getMessage());
            return ReturnData.failed(new FastRunTimeException(ErrorEnum.认证异常));
        }
    }


}

