package com.meta.controller;

import com.meta.model.*;
import com.meta.model.pojo.Account;
import com.meta.model.request.*;
import com.meta.service.AccountServiceImpl;
import com.meta.utils.JWTUtil;
import com.meta.utils.RSAUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(value = "Account 用户", tags = {"Account 用户"})
@RestController
public class AccountController {

    @Autowired
    private AccountServiceImpl accountService;

    @ApiOperation("微信授权登录")
    @PostMapping("/login/wechat")
    public ReturnData<TokenResponse> loginByWechat(@RequestBody LoginByWechatSecretRequest request){
        try{
            LoginByWechatRequest loginByWechatRequest = RSAUtil.decrypt(request.getRequestData(), LoginByWechatRequest.class);
            return ReturnData.success(accountService.loginByWechat(loginByWechatRequest));
        }catch (FastRunTimeException fastRunTimeException){
            return ReturnData.failed(fastRunTimeException);
        }
    }

    @ApiOperation("发送绑定手机号的验证吗")
    @PostMapping("/bind-phone/send-sms-code")
    public ReturnData sendSignUpSmsCode(@RequestBody SendSignUpSmsCodeRequest request){
        try{
            accountService.sendSignUpSmsCode(request.getPhone());
            return ReturnData.success();
        }catch (FastRunTimeException fastRunTimeException){
            return ReturnData.failed(fastRunTimeException);
        }
    }

    @ApiOperation("绑定手机号验证")
    @PostMapping("/bind-phone/check")
    public ReturnData<TokenResponse> loginByWechatCloud(@RequestBody BindPhoneRequest request, @RequestHeader(value = "AccessToken") String token){
        BiteClaims biteClaims = JWTUtil.getBiteClaims(token);
        request.setAccountId(biteClaims.getAccountId());
        request.setTenantId(biteClaims.getTenantId());
        try{
            return ReturnData.success(accountService.bindPhoneCheck(request));
        }catch (FastRunTimeException fastRunTimeException){
            return ReturnData.failed(fastRunTimeException);
        }
    }

    @ApiOperation("查询用户信息")
    @GetMapping("/get/account-info")
    public ReturnData<Account> getAccountById(@RequestHeader(value = "AccessToken") String token){
        try{
            BiteClaims biteClaims = JWTUtil.checkToken(token);
            return ReturnData.success(accountService.getAccountById(biteClaims.getAccountId()));
        }catch (FastRunTimeException fastRunTimeException){
            return ReturnData.failed(fastRunTimeException);
        }
    }

    @ApiOperation("刷新 Token")
    @GetMapping("/refresh/token")
    public ReturnData<TokenResponse> refreshToken(@RequestHeader(value = "RefreshToken") String token){
        try{
            return ReturnData.success(JWTUtil.getAccessTokenAndRefreshTokenByRefreshToken(token));
        }catch (FastRunTimeException fastRunTimeException){
            return ReturnData.failed(fastRunTimeException);
        }
    }
}
