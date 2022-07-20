package com.meta.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.meta.model.*;
import com.meta.model.enums.ReadTypeEnum;
import com.meta.model.pojo.Account;
import com.meta.model.pojo.Document;
import com.meta.model.request.*;
import com.meta.model.response.DeleteDocumentResponse;
import com.meta.model.response.QueryDocumentsResponse;
import com.meta.model.response.UpdateFileResponse;
import com.meta.model.response.UpdateFolderResponse;
import com.meta.service.AccountServiceImpl;
import com.meta.service.DocumentServiceImpl;
import com.meta.utils.*;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.util.*;

@Log4j2
@RestController
public class DocumentController {

    @Resource
    private DocumentServiceImpl documentService;
    @Resource
    private AccountServiceImpl accountService;
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
            documentService.addFile(addDocumentRequest);
            return ReturnData.success();
        }catch (FastRunTimeException fastRunTimeException){
            return ReturnData.failed(fastRunTimeException);
        }
    }

    /**
     * WeChat 新增文件
     * */
    @PostMapping("/wx/documents")
    public ReturnData wxAddFile(@RequestHeader(value = "authorization") String token,
                                @RequestParam(value = "file", required = false) MultipartFile file,
                                @RequestParam("parentId") Long parentId,
                                @RequestParam(value = "name",required = false) String name,
                                @RequestParam(value = "comments",required = false) String comments,
                                HttpServletRequest httpServletRequest){

        Map<String, String[]> map = httpServletRequest.getParameterMap();
        Set<Map.Entry<String, String[]>> keys = map.entrySet();
        Iterator<Map.Entry<String, String[]>> it = keys.iterator();
        while (it.hasNext()) {
            Map.Entry<String, String[]> itMap = it.next();
            log.info("参数--" + itMap.getKey() + ":" + Arrays.toString(itMap.getValue()));
        }
        try{
            MetaClaims claims = oauthJWTUtil.getClaims(token);
            AddDocumentRequest addDocumentRequest = new AddDocumentRequest();
            addDocumentRequest.setParentId(parentId);
            addDocumentRequest.setDocument(file);
            addDocumentRequest.setAccountId(claims.getAccountId());
            addDocumentRequest.setName(name);
            addDocumentRequest.setComments(comments);
            documentService.addFile(addDocumentRequest);
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
            documentService.addFolder(request);
            return ReturnData.success();
        }catch (FastRunTimeException fastRunTimeException){
            return ReturnData.failed(fastRunTimeException);
        }
    }

    /**
     * 编辑文件
     * */
    @PutMapping("/documents/{documentId}")
    public ReturnData<UpdateFileResponse> updateFile(@RequestHeader(value = "authorization") String token, @PathVariable("documentId") Long documentId,
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
            UpdateFileResponse response = documentService.updateFile(request);
            return ReturnData.success(response);
        }catch (FastRunTimeException fastRunTimeException){
            return ReturnData.failed(fastRunTimeException);
        }
    }

    /**
     * 编辑文件夹
     * */
    @PutMapping("/folders/{folderId}")
    public ReturnData<UpdateFolderResponse> updateFolderName(@RequestHeader(value = "authorization") String token, @PathVariable("folderId") Long folderId, @RequestBody UpdateFolderRequest request){
        try{
            MetaClaims claims = oauthJWTUtil.getClaims(token);
            request.setAccountId(claims.getAccountId());
            request.setFolderId(folderId);
            UpdateFolderResponse response = documentService.updateFolder(request);
            return ReturnData.success(response);
        }catch (FastRunTimeException fastRunTimeException){
            return ReturnData.failed(fastRunTimeException);
        }
    }

    /**
     * 删除文件
     * */
    @DeleteMapping("/documents/{documentId}")
    public ReturnData<DeleteDocumentResponse> deleteDocument(@RequestHeader(value = "authorization") String token, @PathVariable(value = "documentId") Long documentId){
        try{
            MetaClaims claims = oauthJWTUtil.getClaims(token);
            DeleteDocumentResponse response = documentService.deleteDocument(documentId, claims.getAccountId());
            return ReturnData.success(response);
        }catch (FastRunTimeException fastRunTimeException){
            return ReturnData.failed(fastRunTimeException);
        }
    }

    @ApiOperation("分页查询7天内的删除")
    @PostMapping("/page/seven-days-ago-delete")
    public ReturnData<Page<Document>> pageDelete(@RequestBody PageDeleteDataRoomRequest request, @RequestHeader(value = "AccessToken") String token){
        try{
            BiteClaims biteClaims = JWTUtil.checkToken(token);
            return ReturnData.success(documentService.pageDelete(request, biteClaims.getTenantId()));
        }catch (FastRunTimeException fastRunTimeException){
            return ReturnData.failed(fastRunTimeException);
        }
    }

    /**
     * 撤销
     * */
    @PostMapping("/undo-log/{undoLogId}")
    public ReturnData restoreDelete(@RequestHeader(value = "authorization") String token, @PathVariable(value = "undoLogId") Long undoLogId){
        try{
            MetaClaims claims = oauthJWTUtil.getClaims(token);
            RollbackByUndoLogRequest request = RollbackByUndoLogRequest.builder().undoLogId(undoLogId).accountId(claims.getAccountId()).build();
            documentService.rollbackByUndoLog(request);
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
            documentService.completeDelete(request.getId(), biteClaims.getTenantId());
            return ReturnData.success();
        }catch (FastRunTimeException fastRunTimeException){
            return ReturnData.failed(fastRunTimeException);
        }
    }

    /**
     * 查询文件夹下的文件
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
            return ReturnData.success(documentService.queryFolder(request));
        }catch (FastRunTimeException fastRunTimeException){
            return ReturnData.failed(fastRunTimeException);
        }
    }

    /**
     * 查询文件
     * @Params
     * type: 查看原始文件还是水印文件
     * */
    @GetMapping("/documents/{documentId}")
    public void getDocument(HttpServletResponse response, @RequestHeader("authorization") String token, @PathVariable("documentId") Long documentId, @RequestParam(name = "type", required = false)ReadTypeEnum type) throws IOException {
        try{
            MetaClaims claims = oauthJWTUtil.getClaims(token);
            Document document = documentService.getFile(documentId, claims.getAccountId());
            log.info("Get documents, documentId:{}, type:{}", documentId, type);
            if (ObjectUtils.isNotEmpty(document)) {
                String downloadFileName = document.getName();
                downloadFileName = URLEncoder.encode(downloadFileName, "utf-8").replace("+", "%20");
                response.setHeader("content-disposition", "attachment;filename*=UTF-8''" + downloadFileName);
                response.setCharacterEncoding("utf-8");
                if (ObjectUtils.isNotEmpty(type) && ReadTypeEnum.ORIGINAL.equals(type)){
                    if (!document.getOperationAccountId().equals(claims.getAccountId())){
                        // 目标文件不是你的文件
                        throw new FastRunTimeException(ErrorEnum.没有文件操作权限);
                    }
                    // 获取原始版本
                    HttpGet get = new HttpGet(document.getUrl());
                    CloseableHttpClient client = HttpClients.createDefault();
                    CloseableHttpResponse httpResponse = client.execute(get);
                    byte[] fileData = EntityUtils.toByteArray(httpResponse.getEntity());
                    IOUtils.closeQuietly(httpResponse);
                    IOUtils.closeQuietly(client);
                    ServletOutputStream outputStream = response.getOutputStream();
                    outputStream.write(fileData);
                }else{
                    // 获取水印版本
                    StringBuilder watermarkTextBuilder = new StringBuilder();
                    Account account = accountService.getAccountById(claims.getAccountId());
                    watermarkTextBuilder.append(account.getName());
                    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                    if (ObjectUtils.isNotEmpty(account.getTimeZone())){
                        String data = TimeZoneFormatUtil.format(timestamp, account.getTimeZone());
                        watermarkTextBuilder.append(" ").append(data);
                    }else{
                        String data = TimeZoneFormatUtil.format(timestamp);
                        watermarkTextBuilder.append(" ").append(data);
                    }
                    PdfUtil.manipulatePdf(document.getUrl(), response.getOutputStream(), watermarkTextBuilder.toString());
                }
            }
        }catch (FastRunTimeException fastRunTimeException){
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(JSONObject.toJSONString(ReturnData.failed(fastRunTimeException)));
        }catch (Exception exception){
            log.info("view a document is error, message:{}", exception.getMessage());
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(JSONObject.toJSONString(ReturnData.failed(new FastRunTimeException(ErrorEnum.网络异常))));
        }
    }

}
