package com.meta.controller;

import com.meta.model.*;
import com.meta.model.pojo.Account;
import com.meta.service.AccountServiceImpl;
import com.meta.utils.OauthJWTUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Api(value = "Account 用户", tags = {"Account 用户"})
@RestController
public class AccountController {

    @Resource
    private AccountServiceImpl accountService;
    @Resource
    private OauthJWTUtil oauthJWTUtil;

    @ApiOperation("查询用户信息")
    @GetMapping("/user")
    public ReturnData<Account> getAccountById(@RequestHeader("authorization") String token){
        try{
            MetaClaims claims = oauthJWTUtil.getClaims(token);
            return ReturnData.success(accountService.getAccountById(claims.getAccountId()));
        }catch (FastRunTimeException fastRunTimeException){
            return ReturnData.failed(fastRunTimeException);
        }
    }

}
