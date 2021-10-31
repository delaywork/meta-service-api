package com.meta.controller;

import com.meta.model.BiteClaims;
import com.meta.model.FastRunTimeException;
import com.meta.model.ReturnData;
import com.meta.model.pojo.DataRoom;
import com.meta.model.request.*;
import com.meta.service.DataRoomServiceImpl;
import com.meta.utils.JWTUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Api(value = "Data Room 文件/文件夹", tags = {"Data Room 文件/文件夹"})
@RestController
public class DataRoomController {

    @Autowired
    private DataRoomServiceImpl dataRoomService;

    @ApiOperation("上传文件")
    @PostMapping("/upload/file")
    public ReturnData addFile(@RequestPart("file") MultipartFile file, @ApiParam(value = "父级菜单") @RequestParam() Long parentId, @RequestHeader(value = "AccessToken") String token){
        try{
            BiteClaims biteClaims = JWTUtil.checkToken(token);
            dataRoomService.addFile(file, biteClaims.getAccountId(), parentId, biteClaims.getTenantId());
            return ReturnData.success();
        }catch (FastRunTimeException fastRunTimeException){
            return ReturnData.failed(fastRunTimeException);
        }
    }

    @ApiOperation("新增文件夹")
    @PostMapping("/add/folder")
    public ReturnData addFolder(@RequestBody AddFolderDataRoomRequest request, @RequestHeader(value = "AccessToken") String token){
        try{
            BiteClaims biteClaims = JWTUtil.checkToken(token);
            DataRoom dataRoom = DataRoom.builder().name(request.getName()).note(request.getNote())
                    .parentId(request.getParentId()).tenantId(biteClaims.getTenantId()).operationAccountId(biteClaims.getAccountId()).build();
            dataRoomService.addFolder(dataRoom);
            return ReturnData.success();
        }catch (FastRunTimeException fastRunTimeException){
            return ReturnData.failed(fastRunTimeException);
        }
    }

    @ApiOperation("重新上传文件")
    @PostMapping("/update/file")
    public ReturnData updateFile(@RequestPart MultipartFile file, @ApiParam(value = "fileId") @RequestParam() Long fileId, @RequestHeader(value = "AccessToken") String token){
        try{
            BiteClaims biteClaims = JWTUtil.checkToken(token);
            dataRoomService.updateFile(file, biteClaims.getAccountId(), fileId, biteClaims.getTenantId());
            return ReturnData.success();
        }catch (FastRunTimeException fastRunTimeException){
            return ReturnData.failed(fastRunTimeException);
        }
    }

    @ApiOperation("修改文件夹名称")
    @PostMapping("/update/folder-name")
    public ReturnData updateFolderName(@RequestBody UpdateFolderNameRequest request, @RequestHeader(value = "AccessToken") String token){
        try{
            BiteClaims biteClaims = JWTUtil.checkToken(token);
            dataRoomService.updateFolderName(request, biteClaims.getAccountId(), biteClaims.getTenantId());
            return ReturnData.success();
        }catch (FastRunTimeException fastRunTimeException){
            return ReturnData.failed(fastRunTimeException);
        }
    }

    @ApiOperation("移动文件/文件夹")
    @PostMapping("/move/data-room")
    public ReturnData moveDataRoom(@RequestBody MoveDataRoomRequest request, @RequestHeader(value = "AccessToken") String token){
        try{
            BiteClaims biteClaims = JWTUtil.checkToken(token);
            dataRoomService.moveDataRoom(request, biteClaims.getAccountId(), biteClaims.getTenantId());
            return ReturnData.success();
        }catch (FastRunTimeException fastRunTimeException){
            return ReturnData.failed(fastRunTimeException);
        }
    }

    @ApiOperation("删除文件/文件夹")
    @PostMapping("/delete/data-room")
    public ReturnData deleteDataRoom(@RequestBody IdRequest request, @RequestHeader(value = "AccessToken") String token){
        try{
            BiteClaims biteClaims = JWTUtil.checkToken(token);
            dataRoomService.deleteDataRoom(request.getId(), biteClaims.getAccountId(), biteClaims.getTenantId());
            return ReturnData.success();
        }catch (FastRunTimeException fastRunTimeException){
            return ReturnData.failed(fastRunTimeException);
        }
    }

    @ApiOperation("根据id查询文件夹的子文件")
    @PostMapping("/get/folder-child")
    public ReturnData<List<DataRoom>> getFolderChild(@RequestBody IdRequest request, @RequestHeader(value = "AccessToken") String token){
        try{
            BiteClaims biteClaims = JWTUtil.checkToken(token);
            return ReturnData.success(dataRoomService.getFolderChild(request.getId(), biteClaims.getAccountId(), biteClaims.getTenantId()));
        }catch (FastRunTimeException fastRunTimeException){
            return ReturnData.failed(fastRunTimeException);
        }
    }

    @ApiOperation("根据id查询文件")
    @PostMapping("/get/file")
    public ReturnData<DataRoom> getFile(@RequestBody IdRequest request, @RequestHeader(value = "AccessToken") String token){
        try{
            // TODO 查询历史版本
            BiteClaims biteClaims = JWTUtil.checkToken(token);
            return ReturnData.success(dataRoomService.getFile(request.getId(), biteClaims.getAccountId(), biteClaims.getTenantId()));
        }catch (FastRunTimeException fastRunTimeException){
            return ReturnData.failed(fastRunTimeException);
        }
    }

    @ApiOperation("下载文件")
    @PostMapping("/download/file")
    public ReturnData<String> downloadPrivate(@RequestBody IdRequest request, @RequestHeader(value = "AccessToken") String token){
        try{
            BiteClaims biteClaims = JWTUtil.checkToken(token);
            return ReturnData.success(dataRoomService.downloadPrivate(request.getId(), biteClaims.getAccountId(), biteClaims.getTenantId()));
        }catch (FastRunTimeException fastRunTimeException){
            return ReturnData.failed(fastRunTimeException);
        }
    }
}
