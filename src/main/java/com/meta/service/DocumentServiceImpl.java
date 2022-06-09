package com.meta.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.meta.mapper.DocumentMapper;
import com.meta.model.ErrorEnum;
import com.meta.model.FastRunTimeException;
import com.meta.model.enums.DocumentCloudEnum;
import com.meta.model.enums.DocumentTypeEnum;
import com.meta.model.enums.OrderEnum;
import com.meta.model.pojo.Document;
import com.meta.model.pojo.VersionHistory;
import com.meta.model.request.*;
import com.meta.model.response.QueryDocumentsResponse;
import com.meta.utils.QiuUtil;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Log4j2
@Service
public class DocumentServiceImpl {

    @Autowired
    private VersionHistoryServiceImpl versionHistoryService;
    @Autowired
    private DocumentMapper documentMapper;
    @Autowired
    private QiuUtil qiuUtil;

    // 根目录
    private final static String ROOT = "/";
    private final static long SEVEN_DAYS_AGO = 1000l * 60l * 60l * 24l * 7l;

    /**
     * 初始化账号根目录
     * */
    public Document initRootFolder(Long accountId, Long tenantId){
        log.info("初始化账号根目录, accountId:{}", accountId);
        if (ObjectUtils.isEmpty(accountId)){
            throw new FastRunTimeException(ErrorEnum.参数不正确);
        }
        QueryWrapper<Document> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Document::getId, accountId)
                .eq(Document::getType, DocumentTypeEnum.FOLDER)
                .eq(Document::getName, ROOT)
                .eq(Document::getDataIsDeleted, false)
                .last("limit 1");
        Document rootDocument = documentMapper.selectOne(queryWrapper);
        if (ObjectUtils.isNotEmpty(rootDocument)){
            log.info("账号下已经存在根目录, accountId:{}", accountId);
            return rootDocument;
        }
        Document document = Document.builder().name(ROOT).operationAccountId(accountId).type(DocumentTypeEnum.FOLDER).tenantId(tenantId).id(accountId).build();
        documentMapper.insert(document);
        return document;
    }

    /**
     * 查询文件（内部调用）
     * */
    public Document getFileInternal(Long fileId){
        return documentMapper.selectById(fileId);
    }

    /**
     * 新增文件
     */
    @Transactional
    public void addFile(AddDocumentRequest request){
        log.info("上传文件， parentId:{}, accountId:{}, name:{}", request.getParentId(), request.getAccountId(), request.getName());
        if (ObjectUtils.isEmpty(request.getDocument()) || ObjectUtils.isEmpty(request.getParentId())){
            throw new FastRunTimeException(ErrorEnum.参数不正确);
        }
        // 获取文件名
        if (ObjectUtils.isEmpty(request.getName())){
            String name = request.getDocument().getOriginalFilename().replace(" ","");
            request.setName(name);
        }
        // 上传文件到七牛
        String url = qiuUtil.uploadStream(request.getDocument());
        // 初始化新增文件
        Document document = new Document();
        document.setParentId(request.getParentId());
        document.setType(DocumentTypeEnum.PDF);
        document.setCloud(DocumentCloudEnum.QIU);
        document.setOperationAccountId(request.getAccountId());
        document.setName(request.getName());
        document.setComments(request.getComments());
        document.setUrl(url);
        documentMapper.insert(document);
    }

    /**
     * 批量新增文件
     * */

    /**
     * 新增文件夹
     * */
    @Transactional
    public void addFolder(AddFolderRequest request){
        log.info("创建文件夹， parentId:{}, accountId:{}, folderName:{}", request.getParentId(), request.getAccountId(), request.getName());
        if (ObjectUtils.isEmpty(request.getParentId()) || ObjectUtils.isEmpty(request.getName())){
            throw new FastRunTimeException(ErrorEnum.参数不正确);
        }
        // 判断文件夹名称是否合法
        if (ROOT.equals(request.getName())){
            throw new FastRunTimeException(ErrorEnum.不能使用根目录命名);
        }
        Document document = Document.builder().parentId(request.getParentId())
                .name(request.getName())
                .operationAccountId(request.getAccountId())
                .comments(request.getComments())
                .type(DocumentTypeEnum.FOLDER).build();
        documentMapper.insert(document);
    }

    /**
     * 修改文件夹
     * */
    @Transactional
    public void updateFolder(UpdateFolderRequest request){
        log.info("修改文件夹信息, accountId:{}, folderId:{}, parentId:{}, name:{}", request.getAccountId(), request.getFolderId(), request.getParentId(), request.getName());
        if (ObjectUtils.isEmpty(request.getFolderId()) || ObjectUtils.isEmpty(request.getAccountId())){
            throw new FastRunTimeException(ErrorEnum.参数不正确);
        }
        // 校验是否能被修改
        Document document = documentMapper.selectById(request.getFolderId());
        if (!DocumentTypeEnum.FOLDER.equals(document.getType())){
            log.info("不是文件夹，不能修改，type:{}", document.getType());
            throw new FastRunTimeException(ErrorEnum.没有文件操作权限);
        }
        if (ROOT.equals(document.getName()) && request.getFolderId().equals(request.getAccountId())){
            throw new FastRunTimeException(ErrorEnum.没有文件操作权限);
        }
        // 编辑文件夹
        if (ObjectUtils.isNotEmpty(request.getParentId())){
            log.info("执行移动文件目录操作");
            this.moveDocument(MoveDocumentRequest.builder().folderId(request.getFolderId())
                    .targetFolderId(request.getParentId())
                    .accountId(request.getAccountId())
                    .build());
            log.info("移动文件目录完成");
        }
        UpdateWrapper<Document> updateWrapper = new UpdateWrapper<>();
        updateWrapper.lambda().eq(Document::getId, request.getFolderId())
                .eq(Document::getDataIsDeleted, false);
        if (ObjectUtils.isNotEmpty(request.getName())){
            log.info("修改文件夹名称");
            updateWrapper.lambda().set(Document::getName, request.getName());
        }
        if (ObjectUtils.isNotEmpty(request.getComments())){
            log.info("修改文件夹备注");
            updateWrapper.lambda().set(Document::getComments, request.getComments());
        }
        updateWrapper.lambda().set(Document::getOperationAccountId, request.getAccountId());
        documentMapper.update(Document.builder().build(), updateWrapper);
    }

    /**
     * 修改文件（将历史记录转移到 VersionHistory）
     * */
    @Transactional
    public void updateFile(UpdateFileRequest request){
        log.info("编辑文件, fileId:{}, accountId:{}, name:{}, parentId:{}", request.getDocumentId(), request.getAccountId(), request.getName(), request.getParentId());
        if (ObjectUtils.isEmpty(request.getDocumentId()) || ObjectUtils.isEmpty(request.getAccountId())){
            throw new FastRunTimeException(ErrorEnum.参数不正确);
        }
        // 校验是否能被修改
        Document document = documentMapper.selectById(request.getDocumentId());
        if (DocumentTypeEnum.FOLDER.equals(document.getType())){
            log.info("不是文件，不能修改，type:{}", document.getType());
            throw new FastRunTimeException(ErrorEnum.没有文件操作权限);
        }
        // 编辑文件
        if (ObjectUtils.isNotEmpty(request.getParentId())){
            log.info("执行移动文件操作");
            this.moveDocument(MoveDocumentRequest.builder().folderId(request.getDocumentId())
                    .targetFolderId(request.getParentId())
                    .accountId(request.getAccountId())
                    .build());
            log.info("移动文件完成");
        }
        UpdateWrapper<Document> updateWrapper = new UpdateWrapper<>();
        updateWrapper.lambda().eq(Document::getId, request.getDocumentId())
                .eq(Document::getDataIsDeleted, false);
        if (ObjectUtils.isNotEmpty(request.getName())){
            log.info("修改文件夹名称");
            updateWrapper.lambda().set(Document::getName, request.getName());
        }
        if (ObjectUtils.isNotEmpty(request.getComments())){
            log.info("修改文件夹备注");
            updateWrapper.lambda().set(Document::getComments, request.getComments());
        }
        if (ObjectUtils.isNotEmpty(request.getFile())){
            log.info("重新上传文件");
            String url = qiuUtil.uploadStream(request.getFile());
            updateWrapper.lambda().set(Document::getUrl, url);
        }
        // 将文件移动到 VersionHistory
//        VersionHistory versionHistory = VersionHistory.builder().dataRoomId(fileId).tenantId(tenantId).note(document.getComments())
//                .operationAccountId(accountId).name(document.getName()).type(document.getType()).url(document.getUrl()).cloud(document.getCloud()).build();
//        versionHistoryService.add(versionHistory);
        documentMapper.update(Document.builder().build(), updateWrapper);
    }

    /**
     * 移动文件/文件夹
     * */
    @Transactional
    public void moveDocument(MoveDocumentRequest request){
        // 校验 targetFolderId 是否是文件夹类型
        Document targetDocument = documentMapper.selectById(request.getTargetFolderId());
        Document document = documentMapper.selectById(request.getFolderId());

        if (ObjectUtils.isEmpty(targetDocument)){
            // 文件不存在
            throw new FastRunTimeException(ErrorEnum.文件不存在);
        }
        if (!DocumentTypeEnum.FOLDER.equals(targetDocument.getType())){
            // 目标不是文件夹类型
            throw new FastRunTimeException(ErrorEnum.不是文件夹类型);
        }
        if (targetDocument.getDataIsDeleted()){
            // 文件夹已被删除
            throw new FastRunTimeException(ErrorEnum.文件已删除);
        }
        // 移动文件夹校验不能移动到子文件夹
        if (DocumentTypeEnum.FOLDER.equals(document.getType())){
            List<Document> folders = new ArrayList<>();
            this.childFolder(document.getId(), folders);
            for (Document folder: folders){
                if (request.getTargetFolderId().equals(folder.getId())){
                    log.info("不能移动到子文件夹");
                    throw new FastRunTimeException(ErrorEnum.不能移动到子文件夹);
                }
            }
        }
        // 修改 folderId 文件父节点
        UpdateWrapper<Document> wrapper = new UpdateWrapper<>();
        wrapper.lambda().eq(Document::getId,request.getFolderId())
                .set(Document::getParentId,request.getTargetFolderId());
        documentMapper.update(Document.builder().build(), wrapper);
    }

    /**
     * 递归查询文件夹
     * */
    private List<Document> childFolder(Long folderId, List<Document> folders){
        QueryWrapper<Document> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(Document::getParentId, folderId)
                .eq(Document::getType, DocumentTypeEnum.FOLDER)
                .eq(Document::getDataIsDeleted, false);
        List<Document> documents = documentMapper.selectList(wrapper);
        if (com.baomidou.mybatisplus.core.toolkit.ObjectUtils.isNotEmpty(documents)){
            folders.addAll(documents);
            documents.forEach(document -> {
                childFolder(document.getId(), folders);
            });
        }
        return folders;
    }

    /**
     * 删除文件/文件夹
     * */
    @Transactional
    public void deleteDataRoom(Long dataRoomId, Long accountId, Long tenantId){
        // 查询dataRoom（如果是文件夹还要遍历删除子文件夹和文件）
        Document document = documentMapper.selectById(dataRoomId);
        if (ObjectUtils.isEmpty(document)){
            // 文件不存在
            throw new FastRunTimeException(ErrorEnum.文件不存在);
        }
        if (document.getTenantId() != tenantId){
            // 目标文件不是你的文件
            throw new FastRunTimeException(ErrorEnum.没有文件操作权限);
        }
        if (document.getDataIsDeleted()){
            // 文件已被删除
            throw new FastRunTimeException(ErrorEnum.文件已删除);
        }
        if (ROOT.equals(document.getName()) && DocumentTypeEnum.FOLDER.equals(document.getType())){
            // 根目录不能被删除
            throw new FastRunTimeException(ErrorEnum.根目录不能被删除);
        }
        // 递归删除子文件
        this.recursiveDelete(dataRoomId);
        // 删除当前目录
        UpdateWrapper<Document> wrapper = new UpdateWrapper<>();
        wrapper.lambda().eq(Document::getId,dataRoomId)
                .set(Document::getDataIsDeleted,true).set(Document::getOperationAccountId, accountId);
        documentMapper.update(null, wrapper);
    }

    /**
     * 递归删除
     * */
    private void recursiveDelete(Long dataRoomId){
        // 判断是否存在子目录
        QueryWrapper<Document> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Document::getParentId, dataRoomId).eq(Document::getDataIsDeleted, false);
        List<Document> documents = documentMapper.selectList(queryWrapper);
        // 子目录不为空继续删除
        if (!ObjectUtils.isEmpty(documents)){
            documents.stream().forEach(item -> {
                if (DocumentTypeEnum.FOLDER.equals(item.getType())){
                    this.recursiveDelete(item.getId());
                }
            });
            UpdateWrapper<Document> updateWrapper = new UpdateWrapper<>();
            updateWrapper.lambda().eq(Document::getParentId, dataRoomId).set(Document::getDataIsDeleted, false);
            documentMapper.update(null, updateWrapper);
        }
    }

    /**
     * 查询7天内的删除列表
     * */
    public Page<Document> pageDelete(PageDeleteDataRoomRequest request, Long tenantId){
        QueryWrapper<Document> wrapper = new QueryWrapper<>();
        long sevenDaysAgo = System.currentTimeMillis() - SEVEN_DAYS_AGO;
        wrapper.lambda().eq(Document::getTenantId, tenantId).eq(Document::getDataIsDeleted, true).ge(Document::getDataUpdateTime,sevenDaysAgo);
        Page<Document> dataRoomPage = documentMapper.selectPage(request.getPage(), wrapper);
        return dataRoomPage;
    }

    /**
     * 恢复删除
     * */
    @Transactional
    public void restoreDelete(Long dataRoomId, Long tenantId){
        // 查询被删除的文件是否属于这个tenant
        QueryWrapper<Document> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(Document::getId, dataRoomId).eq(Document::getDataIsDeleted, true).eq(Document::getTenantId, tenantId);
        Document document = documentMapper.selectOne(wrapper);
        if (ObjectUtils.isEmpty(document)){
            throw new FastRunTimeException(ErrorEnum.文件不存在);
        }
        // 判断父级文件夹是否存在
        QueryWrapper<Document> parentDataRoomWrapper = new QueryWrapper<>();
        parentDataRoomWrapper.lambda().eq(Document::getId, document.getParentId()).eq(Document::getDataIsDeleted, false).eq(Document::getTenantId, tenantId);
        Document parentDocument = documentMapper.selectOne(parentDataRoomWrapper);
        if (ObjectUtils.isEmpty(parentDocument)){
            throw new FastRunTimeException(ErrorEnum.原有父级文件夹不存在);
        }
        // 恢复删除文件
        UpdateWrapper<Document> updateWrapper = new UpdateWrapper<>();
        updateWrapper.lambda().eq(Document::getId,dataRoomId).set(Document::getDataIsDeleted, false);
        documentMapper.update(Document.builder().build(), updateWrapper);
    }

    /**
     * 彻底删除（同步删除历史版本）
     * */
    @Transactional
    public void completeDelete(Long dataRoomId, Long tenantId){
        QueryWrapper<Document> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(Document::getId, dataRoomId).eq(Document::getTenantId, tenantId);
        Document document = documentMapper.selectOne(wrapper);
        if (ObjectUtils.isEmpty(document)){
            throw new FastRunTimeException(ErrorEnum.文件不存在);
        }
        // 删除dataRoom
        documentMapper.deleteById(dataRoomId);
        // 删除历史版本
        versionHistoryService.completeDelete(dataRoomId);
        // 递归彻底删除
        this.recursiveCompleteDelete(dataRoomId);
    }

    /**
     * 彻底递归删除
     * */
    private void recursiveCompleteDelete(Long dataRoomId){
        // 判断是否存在子目录
        QueryWrapper<Document> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Document::getParentId, dataRoomId);
        List<Document> documents = documentMapper.selectList(queryWrapper);
        // 子目录不为空继续删除
        if (!ObjectUtils.isEmpty(documents)){
            documents.stream().forEach(item -> {
                // 删除历史版本
                versionHistoryService.completeDelete(item.getId());
                if (DocumentTypeEnum.FOLDER.equals(item.getType())){
                    this.recursiveCompleteDelete(item.getId());
                }
            });
            // 删除子dataRoom
            documentMapper.delete(queryWrapper);
        }
    }

    /**
     * 根据id查询文件夹的子文件
     * */
    public QueryDocumentsResponse queryFolder(QueryDocumentsRequest request){
        QueryDocumentsResponse response = new QueryDocumentsResponse();
        log.info("查询文件, documentId:{}, accountId:{}",request.getDocumentId(), request.getAccountId());
        if (ObjectUtils.isEmpty(request.getAccountId()) || ObjectUtils.isEmpty(request.getDocumentId())){
            throw new FastRunTimeException(ErrorEnum.参数不正确);
        }
        // 查询当前文件
        QueryWrapper<Document> currentQueryWrapper = new QueryWrapper<>();
        currentQueryWrapper.lambda().eq(Document::getId, request.getDocumentId())
                .eq(Document::getDataIsDeleted, false)
                .last("limit 1");
        Document document = documentMapper.selectOne(currentQueryWrapper);
        response.setCurrentDocument(document);
        // 查询子文件
        if (ObjectUtils.isEmpty(document)){
            log.info("Document 不存在");
            throw new FastRunTimeException(ErrorEnum.文件不存在);
        }
        if (!DocumentTypeEnum.FOLDER.equals(document.getType())){
            log.info("Document 不是文件夹类型, documentType:{}", document.getType());
            return response;
        }
        QueryWrapper<Document> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(Document::getParentId,request.getDocumentId())
                .eq(Document::getDataIsDeleted, false);
        if (ObjectUtils.isNotEmpty(request.getNames())){
            for (int i=0; i<request.getNames().size(); i++){
                if (i == 0){
                    wrapper.lambda().like(Document::getName, request.getNames().get(i));
                }else{
                    wrapper.lambda().or().like(Document::getName, request.getNames().get(i));
                }
            }
        }
        // 排序条件
        if (ObjectUtils.isNotEmpty(request.getOrders())){
            request.getOrders().forEach(order -> {
                if (OrderEnum.DESC.equals(order.getOrder())){
                    wrapper.orderByDesc(order.getColumn());
                }else{
                    wrapper.orderByAsc(order.getColumn());
                }
            });
        }
        wrapper.lambda().orderByDesc(Document::getId);
        // 分页条件
        String limitSql = request.limitSql();
        if (ObjectUtils.isNotEmpty(limitSql)){
            Long total = documentMapper.selectCount(wrapper);
            request.setTotal(total);
            wrapper.last(limitSql);
        }
        List<Document> documents = documentMapper.selectList(wrapper);
        response.setDocuments(documents);
        // 分页返回
        if (ObjectUtils.isNotEmpty(limitSql)){
            response.setPageIndex(request.getPageIndex());
            response.setPageSize(request.getPageSize());
            response.setTotal(request.getTotal());
        }
        return response;
    }

    /**
     * 根据id查询文件
     * */
    public Document getFile(Long documentId, Long accountId){
        if (ObjectUtils.isEmpty(documentId) || ObjectUtils.isEmpty(accountId)){
            throw new FastRunTimeException(ErrorEnum.参数不正确);
        }
        QueryWrapper<Document> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(Document::getId,documentId).eq(Document::getType, DocumentTypeEnum.PDF).eq(Document::getDataIsDeleted, false);
        Document document = documentMapper.selectOne(wrapper);
        if (ObjectUtils.isEmpty(document)){
            // 文件不存在
            throw new FastRunTimeException(ErrorEnum.文件不存在);
        }
        if (!document.getOperationAccountId().equals(accountId)){
            // 目标文件不是你的文件
            throw new FastRunTimeException(ErrorEnum.没有文件操作权限);
        }
        document.setUrl(qiuUtil.downloadPrivate(document.getUrl()));
        return document;
    }

    /**
     * 列表查询文件/文件夹
     * */

    /**
     * 下载文件
     * */
    public String downloadPrivate(Long fileId, Long accountId){
        // 查询文件
        Document file = this.getFile(fileId, accountId);
        if (StringUtils.isEmpty(file.getUrl())){
            // 文件不存在有效地址
            throw new FastRunTimeException(ErrorEnum.文件不存在有效地址);
        }
        try{
            String url = qiuUtil.downloadPrivate(file.getUrl());
            return url;
        }catch (Exception e){
            // 文件下载失败
            log.info("文件下载失败，file:{},accountId:{}",fileId,accountId);
            throw new FastRunTimeException(ErrorEnum.下载失败);
        }
    }

}
