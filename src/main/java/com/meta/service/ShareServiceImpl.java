package com.meta.service;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.Month;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.meta.mapper.ShareMapper;
import com.meta.model.pojo.Account;
import com.meta.model.pojo.DataRoom;
import com.meta.model.pojo.Share;
import com.meta.model.request.shareFileRequest;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Set;

@Log4j2
@Service
public class ShareServiceImpl {

    @Autowired
    private ShareMapper shareMapper;
    @Autowired
    private AccountServiceImpl accountService;
    @Autowired
    private DataRoomServiceImpl dataRoomService;

    /**
     * 分享文件
     * */
    public void shareFile(shareFileRequest request, Long accountId, Long tenantId){
        Account account = accountService.getAccountById(accountId);
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd yyyy", Locale.UK);
        Date date = new Date();
        String dateString = dateFormat.format(date);
        String watermark = account.getName() + " " + dateString;
        Share share = Share.builder().shareAccountId(accountId).tenantId(tenantId).haveWatermark(true).build();
        switch (request.getSourceType()){
            case PDF:
                // 查询文件
                DataRoom file = dataRoomService.getFile(request.getSourceId(), accountId, tenantId);
                // TODO 补充 share 信息
                break;
            case SHARE:
                // 查询分享
                Share oldShare = this.getShare(request.getSourceId());
                // TODO 补充 share 信息
                break;
        }
        // TODO 生成水印文件并存储

    }

    /**
     * 查询分享
     * */
    public Share getShare(Long shareId){
        QueryWrapper<Share> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(Share::getId, shareId).eq(Share::getDataIsDeleted, false);
        Share share = shareMapper.selectOne(wrapper);
        return share;
    }



}
