package com.meta.controller;

import com.meta.model.*;
import com.meta.model.pojo.Account;
import com.meta.model.request.UpdateAccountRequest;
import com.meta.service.AccountServiceImpl;
import com.meta.utils.OauthJWTUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Api(value = "Account 用户", tags = {"Account 用户"})
@RestController
@Log4j2
public class AccountController {

    @Resource
    private AccountServiceImpl accountService;
    @Resource
    private OauthJWTUtil oauthJWTUtil;

    @ApiOperation("查询用户信息")
    @GetMapping("/users/{accountId}")
    public ReturnData<Account> getAccountById(@RequestHeader("authorization") String token, @PathVariable("accountId") Long accountId){
        try{
            MetaClaims claims = oauthJWTUtil.getClaims(token);
            if (!claims.getAccountId().equals(accountId)){
                log.info("只能查询当前用户的信息");
                return ReturnData.failed(new FastRunTimeException(ErrorEnum.权限异常));
            }
            return ReturnData.success(accountService.getAccountById(claims.getAccountId()));
        }catch (FastRunTimeException fastRunTimeException){
            return ReturnData.failed(fastRunTimeException);
        }
    }

    @ApiOperation("修改账户")
    @PatchMapping("/users/{accountId}/profile")
    public ReturnData updateAccount(@RequestHeader("authorization") String token, @RequestBody UpdateAccountRequest request, @PathVariable("accountId") Long accountId){
        try{
            MetaClaims claims = oauthJWTUtil.getClaims(token);
            if (!claims.getAccountId().equals(accountId)){
                log.info("只能修改当前用户的信息");
                return ReturnData.failed(new FastRunTimeException(ErrorEnum.权限异常));
            }
            request.setAccountId(claims.getAccountId());
            accountService.updateAccount(request);
            return ReturnData.success();
        }catch (FastRunTimeException fastRunTimeException){
            return ReturnData.failed(fastRunTimeException);
        }
    }

}
