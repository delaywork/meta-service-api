package com.meta.controller;

import com.meta.model.FastRunTimeException;
import com.meta.model.ReturnData;
import com.meta.model.enums.MetaTermsTypeEnum;
import com.meta.model.pojo.Account;
import com.meta.model.request.GetMetaTermsRequest;
import com.meta.service.MetaServiceImpl;
import com.meta.utils.OauthJWTUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Api(value = "Meta", tags = {"Meta"})
@RestController
public class MetaController {

    @Resource
    private MetaServiceImpl metaService;
    @Resource
    private OauthJWTUtil oauthJWTUtil;

    @ApiOperation("查询用户信息")
    @GetMapping("/meta")
    public ReturnData<Account> getAccountById(@RequestHeader("authorization") String token,  @RequestParam(value = "type", required = false) MetaTermsTypeEnum type){
        try{
            GetMetaTermsRequest request = GetMetaTermsRequest.builder().type(type).build();
            return ReturnData.success(metaService.getMeta(request));
        }catch (FastRunTimeException fastRunTimeException){
            return ReturnData.failed(fastRunTimeException);
        }
    }

}
