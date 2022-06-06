package com.meta.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.meta.mapper.AccountMapper;
import com.meta.model.ErrorEnum;
import com.meta.model.FastRunTimeException;
import com.meta.model.enums.ActivityOperationResourceEnum;
import com.meta.model.enums.ActivityOperationTypeEnum;
import com.meta.model.pojo.Account;
import com.meta.model.pojo.Activity;
import com.meta.model.request.*;
import com.meta.utils.*;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
@Log4j2
public class AccountServiceImpl {

    @Resource
    private AccountMapper accountMapper;
    @Resource
    private TenantServiceImpl tenantService;
    @Resource
    private DocumentServiceImpl dataRoomService;
    @Resource
    private ActivityServiceImpl activityService;
    @Resource
    private WechatUtil wechatUtil;
    @Resource
    private SmsUtil smsUtil;
    @Resource
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
        // 添加操作记录
        Activity termsConditionsActivity = Activity.builder().operationAccount(newAccount.getId())
                .operationResource(ActivityOperationResourceEnum.ACCOUNT)
                .operationResourceId(newAccount.getId())
                .operationType(ActivityOperationTypeEnum.AGREE_TO_TERMS_CONDITIONS).build();
        activityService.addActivity(termsConditionsActivity);
        Activity policyActivity = Activity.builder().operationAccount(newAccount.getId())
                .operationResource(ActivityOperationResourceEnum.ACCOUNT)
                .operationResourceId(newAccount.getId())
                .operationType(ActivityOperationTypeEnum.AGREE_TO_POLICY).build();
        activityService.addActivity(policyActivity);
        return newAccount;
    }

    /**
     * 修改账户
     * */
    public void updateAccount(UpdateAccountRequest request){
        if (ObjectUtils.isEmpty(request.getAccountId())){
            throw new FastRunTimeException(ErrorEnum.帐户不存在);
        }
        log.info("修改账户信息， accountId:{}", request.getAccountId());
        UpdateWrapper<Account> updateWrapper = new UpdateWrapper<>();
        updateWrapper.lambda().eq(Account::getId, request.getAccountId());
        // 设置需要修改的信息
        if (ObjectUtils.isNotEmpty(request.getSetName())){
            updateWrapper.lambda().set(Account::getName, request.getSetName());
        }
        if (ObjectUtils.isNotEmpty(request.getPassword())){
            BCryptPasswordEncoder bcryptPasswordEncoder = new BCryptPasswordEncoder();
            String password = bcryptPasswordEncoder.encode(request.getPassword());
            updateWrapper.lambda().set(Account::getPassword, password);
        }
        if (ObjectUtils.isNotEmpty(request.getAvatarUrl())){
            updateWrapper.lambda().set(Account::getAvatarUrl, request.getAvatarUrl());
        }
        if (ObjectUtils.isNotEmpty(request.getTimeZone())){
            updateWrapper.lambda().set(Account::getTimeZone, request.getTimeZone());
        }
        if (ObjectUtils.isNotEmpty(request.getTimeZoneText())){
            updateWrapper.lambda().set(Account::getTimeZoneText, request.getTimeZoneText());
        }
        if (ObjectUtils.isNotEmpty(request.getLanguageType())){
            updateWrapper.lambda().set(Account::getLanguageType, request.getLanguageType());
        }
        if (ObjectUtils.isNotEmpty(request.getSex())){
            updateWrapper.lambda().set(Account::getSex, request.getSex());
        }
        if (ObjectUtils.isNotEmpty(request.getBirthday())){
            updateWrapper.lambda().set(Account::getBirthday, request.getBirthday());
        }
        accountMapper.update(Account.builder().build(), updateWrapper);
    }

}
