package com.meta.controller;

import com.meta.model.*;
import com.meta.model.request.*;
import com.meta.service.AccountServiceImpl;
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
    public ReturnData<TokenResponse> loginByWechat(@RequestBody loginByWechatRequest request){
        try{
            return ReturnData.success(accountService.loginByWechat(request.getJsCode()));
        }catch (FastRunTimeException fastRunTimeException){
            return ReturnData.failed(fastRunTimeException);
        }
    }

}
