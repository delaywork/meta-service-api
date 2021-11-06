package com.meta.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.Update;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.meta.mapper.DataRoomMapper;
import com.meta.model.ErrorEnum;
import com.meta.model.FastRunTimeException;
import com.meta.model.enums.DataRoomCloudEnum;
import com.meta.model.enums.DataRoomTypeEnum;
import com.meta.model.pojo.DataRoom;
import com.meta.model.pojo.VersionHistory;
import com.meta.model.request.MoveDataRoomRequest;
import com.meta.model.request.PageDeleteDataRoomRequest;
import com.meta.model.request.UpdateFolderNameRequest;
import com.meta.utils.QiuUtil;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Log4j2
@Service
public class DataRoomServiceImpl {

    @Autowired
    private VersionHistoryServiceImpl versionHistoryService;
    @Autowired
    private DataRoomMapper dataRoomMapper;
    @Autowired
    private QiuUtil qiuUtil;

    // 根目录
    private final static String ROOT = "/";
    private final static long SEVEN_DAYS_AGO = 1000l * 60l * 60l * 24l * 7l;

    /**
     * 查询文件（内部调用）
     * */
    public DataRoom getFileInternal(Long fileId){
        return dataRoomMapper.selectById(fileId);
    }

    /**
     * 新增文件
     */
    @Transactional
    public void addFile(MultipartFile file, Long accountId, Long parentId, Long tenantId){
        // 获取文件名
        String name = file.getOriginalFilename().replace(" ","");
        // 上传文件到七牛
        String url = qiuUtil.uploadStream(file);
        // 初始化新增文件
        DataRoom dataRoom = new DataRoom();
        dataRoom.setTenantId(tenantId);
        dataRoom.setParentId(parentId);
        dataRoom.setType(DataRoomTypeEnum.PDF);
        dataRoom.setCloud(DataRoomCloudEnum.QIU);
        dataRoom.setOperationAccountId(accountId);
        dataRoom.setName(name);
        dataRoom.setUrl(url);
        dataRoomMapper.insert(dataRoom);
    }

    /**
     * 批量新增文件
     * */

    /**
     * 新增文件夹
     * */
    @Transactional
    public void addFolder(DataRoom dataRoom){
        // 判断文件夹名称是否合法
        if (ROOT.equals(dataRoom.getName())){
            throw new FastRunTimeException(ErrorEnum.不能使用根目录命名);
        }
        dataRoom.setType(DataRoomTypeEnum.FOLDER);
        dataRoom.setUrl(null);
        dataRoomMapper.insert(dataRoom);
    }

    /**
     * 修改文件（将历史记录转移到 VersionHistory）
     * */
    @Transactional
    public void updateFile(MultipartFile file, Long accountId, Long fileId, Long tenantId){
        DataRoom dataRoom = this.getFile(fileId, accountId, tenantId);
        // 将文件移动到 VersionHistory
        VersionHistory versionHistory = VersionHistory.builder().dataRoomId(fileId).tenantId(tenantId).note(dataRoom.getNote())
                .operationAccountId(accountId).name(dataRoom.getName()).type(dataRoom.getType()).url(dataRoom.getUrl()).cloud(dataRoom.getCloud()).build();
        versionHistoryService.add(versionHistory);
        // 获取文件名
        String name = file.getOriginalFilename().replace(" ","");
        // 上传文件到七牛
        String url = qiuUtil.uploadStream(file);
        UpdateWrapper<DataRoom> wrapper = new UpdateWrapper<>();
        wrapper.lambda().eq(DataRoom::getId, fileId).set(DataRoom::getName,name).set(DataRoom::getUrl,url);
        dataRoomMapper.update(DataRoom.builder().build(), wrapper);
    }

    /**
     * 修改文件夹名称
     * */
    @Transactional
    public void updateFolderName(UpdateFolderNameRequest request, Long accountId, Long tenantId){
        // 判断文件夹名称是否合法
        if (ROOT.equals(request.getName())){
            throw new FastRunTimeException(ErrorEnum.不能使用根目录命名);
        }
        UpdateWrapper<DataRoom> wrapper = new UpdateWrapper<>();
        wrapper.lambda().eq(DataRoom::getId,request.getFolderId()).eq(DataRoom::getTenantId,tenantId)
                .set(DataRoom::getName, request.getName()).set(DataRoom::getOperationAccountId, accountId);
        dataRoomMapper.update(DataRoom.builder().build(), wrapper);
    }

    /**
     * 移动文件/文件夹
     * */
    @Transactional
    public void moveDataRoom(MoveDataRoomRequest request, Long accountId, Long tenantId){
        // 校验 targetFolderId 是否是文件夹类型
        DataRoom dataRoom = dataRoomMapper.selectById(request.getTargetFolderId());
        if (ObjectUtils.isEmpty(dataRoom)){
            // 文件不存在
            throw new FastRunTimeException(ErrorEnum.文件不存在);
        }
        if (dataRoom.getTenantId() != tenantId){
            // 目标文件不是你的文件
            throw new FastRunTimeException(ErrorEnum.没有文件操作权限);
        }
        if (!DataRoomTypeEnum.FOLDER.equals(dataRoom.getType())){
            // 目标不是文件夹类型
            throw new FastRunTimeException(ErrorEnum.不是文件夹类型);
        }
        if (dataRoom.getDataIsDeleted()){
            // 文件夹已被删除
            throw new FastRunTimeException(ErrorEnum.文件已删除);
        }
        // 修改 folderId 文件父节点
        UpdateWrapper<DataRoom> wrapper = new UpdateWrapper<>();
        wrapper.lambda().eq(DataRoom::getId,request.getFolderId())
                .set(DataRoom::getParentId,request.getTargetFolderId());
        dataRoomMapper.update(DataRoom.builder().build(), wrapper);
    }

    /**
     * 删除文件/文件夹
     * */
    @Transactional
    public void deleteDataRoom(Long dataRoomId, Long accountId, Long tenantId){
        // 查询dataRoom（如果是文件夹还要遍历删除子文件夹和文件）
        DataRoom dataRoom = dataRoomMapper.selectById(dataRoomId);
        if (ObjectUtils.isEmpty(dataRoom)){
            // 文件不存在
            throw new FastRunTimeException(ErrorEnum.文件不存在);
        }
        if (dataRoom.getTenantId() != tenantId){
            // 目标文件不是你的文件
            throw new FastRunTimeException(ErrorEnum.没有文件操作权限);
        }
        if (dataRoom.getDataIsDeleted()){
            // 文件已被删除
            throw new FastRunTimeException(ErrorEnum.文件已删除);
        }
        if (ROOT.equals(dataRoom.getName()) && DataRoomTypeEnum.FOLDER.equals(dataRoom.getType())){
            // 根目录不能被删除
            throw new FastRunTimeException(ErrorEnum.根目录不能被删除);
        }
        // 递归删除子文件
        this.recursiveDelete(dataRoomId);
        // 删除当前目录
        UpdateWrapper<DataRoom> wrapper = new UpdateWrapper<>();
        wrapper.lambda().eq(DataRoom::getId,dataRoomId)
                .set(DataRoom::getDataIsDeleted,true).set(DataRoom::getOperationAccountId, accountId);
        dataRoomMapper.update(null, wrapper);
    }

    /**
     * 递归删除
     * */
    private void recursiveDelete(Long dataRoomId){
        // 判断是否存在子目录
        QueryWrapper<DataRoom> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(DataRoom::getParentId, dataRoomId).eq(DataRoom::getDataIsDeleted, false);
        List<DataRoom> dataRooms = dataRoomMapper.selectList(queryWrapper);
        // 子目录不为空继续删除
        if (!ObjectUtils.isEmpty(dataRooms)){
            dataRooms.stream().forEach(item -> {
                if (DataRoomTypeEnum.FOLDER.equals(item.getType())){
                    this.recursiveDelete(item.getId());
                }
            });
            UpdateWrapper<DataRoom> updateWrapper = new UpdateWrapper<>();
            updateWrapper.lambda().eq(DataRoom::getParentId, dataRoomId).set(DataRoom::getDataIsDeleted, false);
            dataRoomMapper.update(null, updateWrapper);
        }
    }

    /**
     * 查询7天内的删除列表
     * */
    public Page<DataRoom> pageDelete(PageDeleteDataRoomRequest request, Long tenantId){
        QueryWrapper<DataRoom> wrapper = new QueryWrapper<>();
        long sevenDaysAgo = System.currentTimeMillis() - SEVEN_DAYS_AGO;
        wrapper.lambda().eq(DataRoom::getTenantId, tenantId).eq(DataRoom::getDataIsDeleted, true).ge(DataRoom::getDataUpdateTime,sevenDaysAgo);
        Page<DataRoom> dataRoomPage = dataRoomMapper.selectPage(request.getPage(), wrapper);
        return dataRoomPage;
    }

    /**
     * 恢复删除
     * */
    @Transactional
    public void restoreDelete(Long dataRoomId, Long tenantId){
        // 查询被删除的文件是否属于这个tenant
        QueryWrapper<DataRoom> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(DataRoom::getId, dataRoomId).eq(DataRoom::getDataIsDeleted, true).eq(DataRoom::getTenantId, tenantId);
        DataRoom dataRoom = dataRoomMapper.selectOne(wrapper);
        if (ObjectUtils.isEmpty(dataRoom)){
            throw new FastRunTimeException(ErrorEnum.文件不存在);
        }
        // 判断父级文件夹是否存在
        QueryWrapper<DataRoom> parentDataRoomWrapper = new QueryWrapper<>();
        parentDataRoomWrapper.lambda().eq(DataRoom::getId,dataRoom.getParentId()).eq(DataRoom::getDataIsDeleted, false).eq(DataRoom::getTenantId, tenantId);
        DataRoom parentDataRoom = dataRoomMapper.selectOne(parentDataRoomWrapper);
        if (ObjectUtils.isEmpty(parentDataRoom)){
            throw new FastRunTimeException(ErrorEnum.原有父级文件夹不存在);
        }
        // 恢复删除文件
        UpdateWrapper<DataRoom> updateWrapper = new UpdateWrapper<>();
        updateWrapper.lambda().eq(DataRoom::getId,dataRoomId).set(DataRoom::getDataIsDeleted, false);
        dataRoomMapper.update(DataRoom.builder().build(), updateWrapper);
    }

    /**
     * 彻底删除（同步删除历史版本）
     * */
    @Transactional
    public void completeDelete(Long dataRoomId, Long tenantId){
        QueryWrapper<DataRoom> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(DataRoom::getId, dataRoomId).eq(DataRoom::getTenantId, tenantId);
        DataRoom dataRoom = dataRoomMapper.selectOne(wrapper);
        if (ObjectUtils.isEmpty(dataRoom)){
            throw new FastRunTimeException(ErrorEnum.文件不存在);
        }
        // 删除dataRoom
        dataRoomMapper.deleteById(dataRoomId);
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
        QueryWrapper<DataRoom> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(DataRoom::getParentId, dataRoomId);
        List<DataRoom> dataRooms = dataRoomMapper.selectList(queryWrapper);
        // 子目录不为空继续删除
        if (!ObjectUtils.isEmpty(dataRooms)){
            dataRooms.stream().forEach(item -> {
                // 删除历史版本
                versionHistoryService.completeDelete(item.getId());
                if (DataRoomTypeEnum.FOLDER.equals(item.getType())){
                    this.recursiveCompleteDelete(item.getId());
                }
            });
            // 删除子dataRoom
            dataRoomMapper.delete(queryWrapper);
        }
    }

    /**
     * 初始化根目录(以用户id作为根目录的id)
     * */
    @Transactional
    public void initFolder(Long accountId, Long tenantId){
        DataRoom dataRoom = DataRoom.builder().id(accountId).type(DataRoomTypeEnum.FOLDER).tenantId(tenantId).name(ROOT).operationAccountId(accountId).build();
        addFolder(dataRoom);
    }

    /**
     * 根据id查询文件夹的子文件
     * */
    public List<DataRoom> getFolderChild(Long dataRoomId, Long accountId, Long tenantId){
        QueryWrapper<DataRoom> wrapper = new QueryWrapper<>();
        if (ObjectUtils.isEmpty(dataRoomId)){
            // 目录为空查询根目录
            wrapper.lambda().eq(DataRoom::getId, accountId);
        }else{
            // 目录不为空查询目录
            wrapper.lambda().eq(DataRoom::getId, dataRoomId);
        }
        wrapper.lambda().eq(DataRoom::getParentId,dataRoomId).eq(DataRoom::getTenantId, tenantId).eq(DataRoom::getDataIsDeleted, false)
                .orderByDesc(DataRoom::getId);
        List<DataRoom> dataRooms = dataRoomMapper.selectList(wrapper);
        return dataRooms;
    }

    /**
     * 根据id查询文件
     * */
    public DataRoom getFile(Long dataRoomId, Long accountId, Long tenantId){
        QueryWrapper<DataRoom> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(DataRoom::getId,dataRoomId).eq(DataRoom::getType,DataRoomTypeEnum.PDF).eq(DataRoom::getDataIsDeleted, false);
        DataRoom dataRoom = dataRoomMapper.selectOne(wrapper);
        if (ObjectUtils.isEmpty(dataRoom)){
            // 文件不存在
            throw new FastRunTimeException(ErrorEnum.文件不存在);
        }
        if (!dataRoom.getTenantId().equals(tenantId)){
            // 目标文件不是你的文件
            throw new FastRunTimeException(ErrorEnum.没有文件操作权限);
        }
        dataRoom.setUrl(qiuUtil.downloadPrivate(dataRoom.getUrl()));
        return dataRoom;
    }

    /**
     * 列表查询文件/文件夹
     * */

    /**
     * 下载文件
     * */
    public String downloadPrivate(Long fileId, Long accountId, Long tenantId){
        // 查询文件
        DataRoom file = this.getFile(fileId, accountId, tenantId);
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
