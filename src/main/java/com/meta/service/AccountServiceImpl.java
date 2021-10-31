package com.meta.service;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.meta.mapper.AccountMapper;
import com.meta.mapper.TenantMapper;
import com.meta.model.TokenResponse;
import com.meta.model.WechatUtilLoginResponse;
import com.meta.model.pojo.Account;
import com.meta.model.pojo.Tenant;
import com.meta.model.request.IdRequest;
import com.meta.model.request.LoginByWechatCloudDTO;
import com.meta.model.request.LoginByWechatCloudRequest;
import com.meta.utils.JWTUtil;
import com.meta.utils.WechatUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Log4j2
@Service
public class AccountServiceImpl {

    @Autowired
    private AccountMapper accountMapper;
    @Autowired
    private TenantMapper tenantMapper;
    @Autowired
    private WechatUtil wechatUtil;

    /**
     * 小程序登录验证
     * */
    public TokenResponse loginByWechat(String jsCode){
        WechatUtilLoginResponse wechatLoginResponse = wechatUtil.login(jsCode);
        // 判断是否存在账号，没有则创建账号
        QueryWrapper<Account> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(Account::getOpenid, wechatLoginResponse.getOpenid());
        Account account = accountMapper.selectOne(wrapper);
        Long tenantId = null;
        if (ObjectUtil.isEmpty(account)){
            // 创建Tenant
            Tenant tenant = Tenant.builder().build();
            tenantMapper.insert(tenant);
            // 创建Account
            account = Account.builder().openid(wechatLoginResponse.getOpenid()).unionid(wechatLoginResponse.getUnionid()).wechatAuth(true).tenantId(tenant.getId()).build();
            accountMapper.insert(account);
        }
        tenantId = account.getTenantId();
        // 生成 token
        TokenResponse tokenResponse = JWTUtil.getAccessTokenAndRefreshToken(account.getId(), tenantId);
        return tokenResponse;
    }

    /**
     * 微信云函数登录验证
     * */
    public TokenResponse loginByWechatCloud(LoginByWechatCloudDTO request){
        // 判断是否存在账号，没有则创建账号
        QueryWrapper<Account> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(Account::getOpenid, request.getOpenid());
        Account account = accountMapper.selectOne(wrapper);
        Long tenantId = null;
        if (ObjectUtil.isEmpty(account)){
            // 创建Tenant
            Tenant tenant = Tenant.builder().build();
            tenantMapper.insert(tenant);
            // 创建Account
            account = Account.builder().openid(request.getOpenid()).unionid(request.getUnionid()).name(request.getName()).wechatAuth(true).tenantId(tenant.getId()).build();
            accountMapper.insert(account);
        }
        tenantId = account.getTenantId();
        // 生成 token
        TokenResponse tokenResponse = JWTUtil.getAccessTokenAndRefreshToken(account.getId(), tenantId);
        return tokenResponse;
    }

    /**
     * 发送注册邮件
     * */

    /**
     * 注册
     * */

    /**
     * 登录
     * */

    /**
     * 登出
     * */

    /**
     * 查询账户信息
     * */
    public Account getAccountById(Long accountId){
        QueryWrapper<Account> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(Account::getId, accountId).eq(Account::getDataIsDeleted, false);
        Account account = accountMapper.selectOne(wrapper);
        return account;
    }



}
