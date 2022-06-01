package com.meta.controller;

import com.meta.model.FastRunTimeException;
import com.meta.model.ReturnData;
import com.meta.model.enums.InfoTypeEnum;
import com.meta.model.pojo.Account;
import com.meta.model.request.GetInfoTermsRequest;
import com.meta.service.InfoServiceImpl;
import com.meta.utils.OauthJWTUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Api(value = "Meta", tags = {"Meta"})
@RestController
public class InfoController {

    @Resource
    private InfoServiceImpl infoService;
    @Resource
    private OauthJWTUtil oauthJWTUtil;

    @ApiOperation("查询 meta 信息")
    @GetMapping("/info")
    public ReturnData<Account> getAccountById(@RequestParam(value = "type", required = false) InfoTypeEnum type){
        try{
            GetInfoTermsRequest request = GetInfoTermsRequest.builder().type(type).build();
            return ReturnData.success(infoService.getMeta(request));
        }catch (FastRunTimeException fastRunTimeException){
            return ReturnData.failed(fastRunTimeException);
        }
    }

}
