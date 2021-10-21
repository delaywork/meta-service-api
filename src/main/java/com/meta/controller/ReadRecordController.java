package com.meta.controller;

import com.meta.model.BiteClaims;
import com.meta.model.FastRunTimeException;
import com.meta.model.ReturnData;
import com.meta.model.request.*;
import com.meta.model.response.ReadRecordResponse;
import com.meta.service.ReadScheduleServiceImpl;
import com.meta.service.ReadTimeServiceImpl;
import com.meta.utils.JWTUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

@Api(value = "read record 阅读记录", tags = {"read record 阅读记录"})
@RestController
public class ReadRecordController {

    @Autowired
    private ReadScheduleServiceImpl readScheduleService;
    @Autowired
    private ReadTimeServiceImpl readTimeService;

    @ApiOperation("记录阅读数据")
    @PostMapping("/add/read-record")
    public ReturnData readRecord(@RequestBody ReadRecordRequest request, @RequestHeader(value = "AccessToken") String token){
        try{
            BiteClaims biteClaims = JWTUtil.checkToken(token);
            readScheduleService.readRecord(request, biteClaims.getAccountId());
            return ReturnData.success();
        }catch (FastRunTimeException fastRunTimeException){
            return ReturnData.failed(fastRunTimeException);
        }
    }

    @ApiOperation("阅读记录全量查询")
    @PostMapping("/query/read-record")
    public ReturnData<ReadRecordResponse> queryReadRecord(@RequestBody GetRecordByReadSourceRequest request, @RequestHeader(value = "AccessToken") String token){
        try{
            BiteClaims biteClaims = JWTUtil.checkToken(token);
            return ReturnData.success(readScheduleService.queryReadRecord(request, biteClaims.getAccountId()));
        }catch (FastRunTimeException fastRunTimeException){
            return ReturnData.failed(fastRunTimeException);
        }
    }


}
