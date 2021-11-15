package com.meta.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.meta.mapper.ShareMapper;
import com.meta.model.ErrorEnum;
import com.meta.model.FastRunTimeException;
import com.meta.model.enums.ShareSourceTypeEnum;
import com.meta.model.pojo.Account;
import com.meta.model.pojo.DataRoom;
import com.meta.model.pojo.Share;
import com.meta.model.request.ShareFileRequest;
import com.meta.utils.PdfUtil;
import com.meta.utils.QiuUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@Log4j2
@Service
public class ShareServiceImpl {

    @Autowired
    private ShareMapper shareMapper;
    @Autowired
    private AccountServiceImpl accountService;
    @Autowired
    private DataRoomServiceImpl dataRoomService;
    @Autowired
    private QiuUtil qiuUtil;

    /**
     * 分享文件
     * */
    @Transactional
    public String shareFile(ShareFileRequest request, Long accountId, Long tenantId){
        Account account = accountService.getAccountById(accountId);
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd yyyy", Locale.UK);
        Date date = new Date();
        String dateString = dateFormat.format(date);
        String watermark = account.getName() + " share in " + dateString;
        Share share = Share.builder().shareAccountId(accountId).tenantId(tenantId).haveWatermark(true).watermarkText(watermark).build();
        switch (request.getSourceType()){
            case PDF:
                // 查询文件
                DataRoom file = dataRoomService.getFile(request.getSourceId(), accountId, tenantId);
                // 补充 share 信息
                share.setSourceId(file.getId());
                share.setSourceType(ShareSourceTypeEnum.PDF);
                share.setSourceUrl(file.getUrl());
                share.setName(file.getName());
                break;
            case SHARE:
                // 查询分享
                Share oldShare = this.getShare(request.getSourceId(), accountId);
                // 判断该分享是否可以被其他用户分享
                if (!share.getShareAccountId().equals(accountId) && !share.getAllowShare()){
                    throw new FastRunTimeException(ErrorEnum.该分享已被禁止其他用户分享);
                }
                // 补充 share 信息
                share.setSourceId(oldShare.getId());
                share.setSourceType(ShareSourceTypeEnum.SHARE);
                share.setSourceUrl(oldShare.getSourceUrl());
                share.setName(oldShare.getName());
                break;
        }
        // 生成水印文件并存储
        try{
            MultipartFile multipartFile = PdfUtil.manipulatePdfReturnFile(qiuUtil.downloadPrivate(share.getSourceUrl()), watermark, share.getName());
            String url = qiuUtil.uploadStream(multipartFile);
            share.setWatermarkUrl(url);
        }catch (Exception e){
            log.info("pdf 加水印失败，{}",e.getMessage());
            throw new FastRunTimeException(ErrorEnum.添加水印失败);
        }
        shareMapper.insert(share);
        return qiuUtil.downloadPrivate(share.getWatermarkUrl());
    }

    /**
     * 查询分享
     * */
    public Share getShare(Long shareId, Long accountId){
        QueryWrapper<Share> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(Share::getId, shareId).eq(Share::getDataIsDeleted, false);
        Share share = shareMapper.selectOne(wrapper);
        // 判断这个分享是否可被其他用户查看
        if (!share.getShareAccountId().equals(accountId) && !share.getAllowAccess()){
            throw new FastRunTimeException(ErrorEnum.该分享已被禁止其他用户访问);
        }
        // 获取可公开访问的水印文件
        String publicWatermarkUrl = qiuUtil.downloadPrivate(share.getWatermarkUrl());
        share.setWatermarkUrl(publicWatermarkUrl);
        return share;
    }

    /**
     * 查询文件（内部调用）
     * */
    public Share getShareInternal(Long shareId){
        return shareMapper.selectById(shareId);
    }



}
