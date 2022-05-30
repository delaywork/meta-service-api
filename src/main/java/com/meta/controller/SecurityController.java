package com.meta.controller;

import com.meta.model.FastRunTimeException;
import com.meta.model.MetaClaims;
import com.meta.model.ReturnData;
import com.meta.model.enums.VerificationMethodEnum;
import com.meta.model.pojo.Account;
import com.meta.model.request.AddVerificationRequest;
import com.meta.model.request.SecurityRequest;
import com.meta.service.SecurityServiceImpl;
import com.meta.utils.OauthJWTUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
public class SecurityController {

    @Resource
    private SecurityServiceImpl securityService;
    @Resource
    private OauthJWTUtil oauthJWTUtil;

    @ApiOperation("发送验证码")
    @PostMapping("/verification")
    public ReturnData addVerification(@RequestHeader("authorization") String token, @RequestBody AddVerificationRequest request){
        try{
            securityService.addVerification(request);
            return ReturnData.success();
        }catch (FastRunTimeException fastRunTimeException){
            return ReturnData.failed(fastRunTimeException);
        }
    }

    @ApiOperation("验证")
    @PatchMapping("/security/{type}")
    public ReturnData<Account> security(@RequestHeader("authorization") String token, @PathVariable("type") VerificationMethodEnum type, @RequestBody SecurityRequest request){
        try{
            MetaClaims claims = oauthJWTUtil.getClaims(token);
            request.setAccountId(claims.getAccountId());
            request.setMethod(type);
            securityService.security(request);
            return ReturnData.success();
        }catch (FastRunTimeException fastRunTimeException){
            return ReturnData.failed(fastRunTimeException);
        }
    }

}
