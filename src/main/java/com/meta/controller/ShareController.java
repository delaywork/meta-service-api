package com.meta.controller;

import com.meta.model.BiteClaims;
import com.meta.model.FastRunTimeException;
import com.meta.model.ReturnData;
import com.meta.model.request.ShareFileRequest;
import com.meta.service.ShareServiceImpl;
import com.meta.utils.JWTUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "Share 分享", tags = {"Share 分享"})
@RestController
public class ShareController {

    @Autowired
    private ShareServiceImpl shareService;

    @ApiOperation("分享")
    @PostMapping("/share/file")
    public ReturnData<String> shareFile(@RequestBody ShareFileRequest request, @RequestHeader(value = "AccessToken") String token){
        try{
            BiteClaims biteClaims = JWTUtil.checkToken(token);
            String url = shareService.shareFile(request, biteClaims.getAccountId(), biteClaims.getTenantId());
            return ReturnData.success(url);
        }catch (FastRunTimeException fastRunTimeException){
            return ReturnData.failed(fastRunTimeException);
        }
    }

}
