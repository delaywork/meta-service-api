package com.meta.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.meta.model.*;
import com.meta.model.pojo.Document;
import com.meta.model.request.*;
import com.meta.model.response.QueryDocumentsResponse;
import com.meta.service.DocumentServiceImpl;
import com.meta.utils.JWTUtil;
import com.meta.utils.OauthJWTUtil;
import com.meta.utils.ParamsUtil;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.List;

@RestController
public class DocumentController {

    @Resource
    private DocumentServiceImpl dataRoomService;
    @Resource
    private OauthJWTUtil oauthJWTUtil;

    /**
     * 新增文件
     * */
    @PostMapping("/documents")
    public ReturnData addFile(@RequestHeader(value = "authorization") String token, @RequestPart("document") MultipartFile document,
                              @RequestParam("parentId") Long parentId, @RequestParam(value = "name",required = false) String name,
                              @RequestParam(value = "comments",required = false) String comments){
        try{
            MetaClaims claims = oauthJWTUtil.getClaims(token);
            AddDocumentRequest addDocumentRequest = new AddDocumentRequest();
            addDocumentRequest.setParentId(parentId);
            addDocumentRequest.setDocument(document);
            addDocumentRequest.setAccountId(claims.getAccountId());
            addDocumentRequest.setName(name);
            addDocumentRequest.setComments(comments);
            dataRoomService.addFile(addDocumentRequest);
            return ReturnData.success();
        }catch (FastRunTimeException fastRunTimeException){
            return ReturnData.failed(fastRunTimeException);
        }
    }

    /**
     * 新增文件夹
     * */
    @PostMapping("/folders")
    public ReturnData addFolder(@RequestHeader(value = "authorization") String token, @RequestBody AddFolderRequest request){
        try{
            MetaClaims claims = oauthJWTUtil.getClaims(token);
            request.setAccountId(claims.getAccountId());
            dataRoomService.addFolder(request);
            return ReturnData.success();
        }catch (FastRunTimeException fastRunTimeException){
            return ReturnData.failed(fastRunTimeException);
        }
    }

    /**
     * 编辑文件
     * */
    @PutMapping("/documents/{documentId}")
    public ReturnData updateFile(@RequestHeader(value = "authorization") String token, @PathVariable("documentId") Long documentId,
                                 @RequestPart(value = "file", required = false) MultipartFile file, @RequestParam(value = "name", required = false) String name,
                                 @RequestParam(value = "parentId", required = false) Long parentId, @RequestParam(value = "comments", required = false) String comments){
        try{
            MetaClaims claims = oauthJWTUtil.getClaims(token);
            UpdateFileRequest request = UpdateFileRequest.builder().accountId(claims.getAccountId())
                    .documentId(documentId)
                    .parentId(parentId)
                    .name(name)
                    .comments(comments)
                    .file(file)
                    .build();
            dataRoomService.updateFile(request);
            return ReturnData.success();
        }catch (FastRunTimeException fastRunTimeException){
            return ReturnData.failed(fastRunTimeException);
        }
    }

    /**
     * 编辑文件夹
     * */
    @PutMapping("/folders/{folderId}")
    public ReturnData updateFolderName(@RequestHeader(value = "authorization") String token, @PathVariable("folderId") Long folderId, @RequestBody UpdateFolderRequest request){
        try{
            MetaClaims claims = oauthJWTUtil.getClaims(token);
            request.setAccountId(claims.getAccountId());
            request.setFolderId(folderId);
            dataRoomService.updateFolder(request);
            return ReturnData.success();
        }catch (FastRunTimeException fastRunTimeException){
            return ReturnData.failed(fastRunTimeException);
        }
    }

    @ApiOperation("删除文件/文件夹")
    @PostMapping("/delete/data-room")
    public ReturnData deleteDataRoom(@RequestBody @ApiParam(value = "dataRoomId") IdRequest request, @RequestHeader(value = "AccessToken") String token){
        try{
            BiteClaims biteClaims = JWTUtil.checkToken(token);
            dataRoomService.deleteDataRoom(request.getId(), biteClaims.getAccountId(), biteClaims.getTenantId());
            return ReturnData.success();
        }catch (FastRunTimeException fastRunTimeException){
            return ReturnData.failed(fastRunTimeException);
        }
    }

    @ApiOperation("分页查询7天内的删除")
    @PostMapping("/page/seven-days-ago-delete")
    public ReturnData<Page<Document>> pageDelete(@RequestBody PageDeleteDataRoomRequest request, @RequestHeader(value = "AccessToken") String token){
        try{
            BiteClaims biteClaims = JWTUtil.checkToken(token);
            return ReturnData.success(dataRoomService.pageDelete(request, biteClaims.getTenantId()));
        }catch (FastRunTimeException fastRunTimeException){
            return ReturnData.failed(fastRunTimeException);
        }
    }

    @ApiOperation("恢复删除")
    @PostMapping("/restore/delete")
    public ReturnData restoreDelete(@RequestBody @ApiParam(value = "dataRoomId") IdRequest request, @RequestHeader(value = "AccessToken") String token){
        try{
            BiteClaims biteClaims = JWTUtil.checkToken(token);
            dataRoomService.restoreDelete(request.getId(), biteClaims.getTenantId());
            return ReturnData.success();
        }catch (FastRunTimeException fastRunTimeException){
            return ReturnData.failed(fastRunTimeException);
        }
    }

    @ApiOperation("彻底删除（同步删除历史版本）")
    @PostMapping("/complete/delete")
    @Transactional
    public ReturnData completeDelete(@RequestBody @ApiParam(value = "dataRoomId") IdRequest request, @RequestHeader(value = "AccessToken") String token){
        try{
            BiteClaims biteClaims = JWTUtil.checkToken(token);
            dataRoomService.completeDelete(request.getId(), biteClaims.getTenantId());
            return ReturnData.success();
        }catch (FastRunTimeException fastRunTimeException){
            return ReturnData.failed(fastRunTimeException);
        }
    }

    /**
     * 查询文件
     * */
    @GetMapping("/folders/{folderId}/documents")
    public ReturnData<QueryDocumentsResponse> queryFolders(@RequestHeader("authorization") String token, @PathVariable("folderId") Long folderId,
                                                           @RequestParam(value = "name", required = false) String name,
                                                           @RequestParam(value = "sort", required = false) String sort,
                                                           @RequestParam(value = "pageIndex", required = false) Long pageIndex,
                                                           @RequestParam(value = "pageSize", required = false) Long pageSize){
        try{
            MetaClaims claims = oauthJWTUtil.getClaims(token);
            QueryDocumentsRequest request = new QueryDocumentsRequest();
            request.setDocumentId(folderId);
            request.setAccountId(claims.getAccountId());
            request.setPageIndex(pageIndex);
            request.setPageSize(pageSize);
            if (ObjectUtils.isNotEmpty(name)){
                List<String> names = ParamsUtil.getStringParams(name);
                request.setNames(names);
            }
            if (ObjectUtils.isNotEmpty(sort)){
                List<Order> orders = ParamsUtil.getOrderParams(sort);
                request.setOrders(orders);
            }
            return ReturnData.success(dataRoomService.queryFolder(request));
        }catch (FastRunTimeException fastRunTimeException){
            return ReturnData.failed(fastRunTimeException);
        }
    }

    @ApiOperation("根据id查询文件")
    @PostMapping("/get/file")
    public ReturnData<Document> getFile(@RequestBody @ApiParam(value = "dataRoomId") IdRequest request, @RequestHeader(value = "AccessToken") String token){
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
    public ReturnData<String> downloadPrivate(@RequestBody @ApiParam(value = "dataRoomId") IdRequest request, @RequestHeader(value = "AccessToken") String token){
        try{
            BiteClaims biteClaims = JWTUtil.checkToken(token);
            return ReturnData.success(dataRoomService.downloadPrivate(request.getId(), biteClaims.getAccountId(), biteClaims.getTenantId()));
        }catch (FastRunTimeException fastRunTimeException){
            return ReturnData.failed(fastRunTimeException);
        }
    }
}
