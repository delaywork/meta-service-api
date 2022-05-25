package com.meta.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.meta.mapper.AccountMapper;
import com.meta.model.pojo.Account;
import com.meta.model.request.*;
import com.meta.utils.*;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class AccountServiceImpl {

    @Autowired
    private AccountMapper accountMapper;
    @Autowired
    private TenantServiceImpl tenantService;
    @Autowired
    private DataRoomServiceImpl dataRoomService;
    @Autowired
    private WechatUtil wechatUtil;
    @Autowired
    private SmsUtil smsUtil;
    @Autowired
    private RedisUtil redisUtil;

    /**
     * 查询账户信息
     * */
    public Account getAccountById(Long accountId){
        QueryWrapper<Account> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(Account::getId, accountId).eq(Account::getDataIsDeleted, false);
        Account account = accountMapper.selectOne(wrapper);
        return account;
    }

    /**
     * 查询账号
     * */
    public Account getAccount(GetAccountRequest request){
        QueryWrapper<Account> wrapper = new QueryWrapper<>();
        if (ObjectUtils.isNotEmpty(request.getAccountId())){
            wrapper.lambda().eq(Account::getId, request.getAccountId());
        }
        if (ObjectUtils.isNotEmpty(request.getOpenid())){
            wrapper.lambda().eq(Account::getOpenid, request.getOpenid());
        }
        if (ObjectUtils.isNotEmpty(request.getUnionid())){
            wrapper.lambda().eq(Account::getUnionid, request.getUnionid());
        }
        wrapper.lambda().last("limit 1");
        Account account = accountMapper.selectOne(wrapper);
        return account;
    }

    /**
     * 创建账号
     * */
    public Account addAccount(AddAccountRequest request){
        // 校验账户是否存在
        Account account = this.getAccount(GetAccountRequest.builder().openid(request.getOpenid()).unionid(request.getUnionid()).build());
        if (ObjectUtils.isNotEmpty(account)){
            log.info("addAccount ---> 账号已经存在，accountId:{}", account.getId());
            return account;
        }
        // 创建 tenant
        Long tenantId = tenantService.addTenant();
        // 创建 account
        Account newAccount = new Account();
        newAccount.setOpenid(request.getOpenid());
        newAccount.setUnionid(request.getUnionid());
        newAccount.setName(request.getName());
        newAccount.setAvatarUrl(request.getAvatarUrl());
        if (ObjectUtils.isNotEmpty(request.getPassword())){
            BCryptPasswordEncoder bcryptPasswordEncoder = new BCryptPasswordEncoder();
            String password = bcryptPasswordEncoder.encode(request.getPassword());
            newAccount.setPassword(password);
        }
        newAccount.setTenantId(tenantId);
        accountMapper.insert(newAccount);
        // 初始化根目录
        dataRoomService.initRootFolder(newAccount.getId(), tenantId);
        return newAccount;
    }

}
