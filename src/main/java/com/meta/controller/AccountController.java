package com.meta.controller;

import com.meta.model.*;
import com.meta.model.pojo.Account;
import com.meta.model.request.*;
import com.meta.service.AccountServiceImpl;
import com.meta.utils.CodeUtil;
import com.meta.utils.JWTUtil;
import com.meta.utils.RSAUtil;
import com.meta.utils.RedisKeys;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.TimeUnit;

@Api(value = "Account 用户", tags = {"Account 用户"})
@RestController
public class AccountController {

    @Autowired
    private AccountServiceImpl accountService;

    @ApiOperation("微信授权登录")
    @PostMapping("/login/wechat")
    public ReturnData<TokenResponse> loginByWechat(@RequestBody loginByWechatRequest request){
        try{
            return ReturnData.success(accountService.loginByWechat(request.getJsCode()));
        }catch (FastRunTimeException fastRunTimeException){
            return ReturnData.failed(fastRunTimeException);
        }
    }

    @ApiOperation("微信云函数登录验证")
    @PostMapping("/login/wechat-cloud")
    public ReturnData<TokenResponse> loginByWechatCloud(@RequestBody LoginByWechatCloudRequest request){
        if (StringUtils.isEmpty(request.getRequestData())){
            return ReturnData.failed(new FastRunTimeException(ErrorEnum.参数不正确));
        }
        try{
            LoginByWechatCloudDTO loginByWechatCloudDTO = RSAUtil.decrypt(request.getRequestData(), LoginByWechatCloudDTO.class);
            return ReturnData.success(accountService.loginByWechatCloud(loginByWechatCloudDTO));
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

    @PostMapping("/sign-up/send-sms-code")
    public ReturnData sendSignUpSmsCode(@RequestBody SendSignUpSmsCodeRequest request){
        try{
            accountService.sendSignUpSmsCode(request.getPhone());
            return ReturnData.success();
        }catch (FastRunTimeException fastRunTimeException){
            return ReturnData.failed(fastRunTimeException);
        }
    }

}
