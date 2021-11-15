package com.meta.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.meta.model.BiteClaims;
import com.meta.model.FastRunTimeException;
import com.meta.model.ReturnData;
import com.meta.model.enums.AccountTypeEnum;
import com.meta.model.pojo.*;
import com.meta.model.request.*;
import com.meta.model.response.AddBlankReadRecordResponse;
import com.meta.model.response.ReadRecordResponse;
import com.meta.model.response.vo.ReadRecordByAccountVO;
import com.meta.model.response.vo.ReadRecordBySourceVO;
import com.meta.service.AccountServiceImpl;
import com.meta.service.ReadScheduleServiceImpl;
import com.meta.service.ReadTimeServiceImpl;
import com.meta.utils.JWTUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Api(value = "read record 阅读记录", tags = {"read record 阅读记录"})
@RestController
public class ReadRecordController {

    @Autowired
    private ReadScheduleServiceImpl readScheduleService;
    @Autowired
    private ReadTimeServiceImpl readTimeService;
    @Autowired
    private AccountServiceImpl accountService;

    @ApiOperation("记录阅读数据（轮询方式，默认间隔时间：3s）")
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

    @ApiOperation("新增空白阅读记录，WebSocket记录阅读信息前调用")
    @PostMapping("/add/blank-read-record")
    private ReturnData<AddBlankReadRecordResponse> addBlankReadRecord(@RequestBody AddBlankReadRecordRequest request, @RequestHeader(value = "AccessToken") String token){
        try{
            BiteClaims biteClaims = JWTUtil.checkToken(token);
            ReadSchedule readSchedule = ReadSchedule.builder().accountId(biteClaims.getAccountId()).accountType(AccountTypeEnum.MEMBERS)
                    .sourceId(request.getSourceId()).sourceType(request.getSourceType()).build();
            return ReturnData.success( readScheduleService.addBlankReadRecord(readSchedule));
        }catch (FastRunTimeException fastRunTimeException){
            return ReturnData.failed(fastRunTimeException);
        }
    }

    @ApiOperation("阅读记录分页查询（根据accountId）")
    @PostMapping("/page/read-record/by-account")
    public ReturnData<Page<ReadRecordByAccountVO>> pageReadRecordByAccount(@RequestBody PageReadRecordByAccountRequest request, @RequestHeader(value = "AccessToken") String token){
        try{
            BiteClaims biteClaims = JWTUtil.checkToken(token);
            return ReturnData.success( readScheduleService.pageReadRecordByAccount(request, biteClaims.getAccountId()));
        }catch (FastRunTimeException fastRunTimeException){
            return ReturnData.failed(fastRunTimeException);
        }
    }

    @ApiOperation("阅读记录分页查询（根据源id）")
    @PostMapping("/page/read-record/by-source")
    public ReturnData<Page<ReadRecordBySourceVO>> pageReadRecordBySource(@RequestBody PageReadRecordBySourceRequest request, @RequestHeader(value = "AccessToken") String token){
        try{
            BiteClaims biteClaims = JWTUtil.checkToken(token);
            return ReturnData.success( readScheduleService.pageReadRecordBySource(request));
        }catch (FastRunTimeException fastRunTimeException){
            return ReturnData.failed(fastRunTimeException);
        }
    }




}
