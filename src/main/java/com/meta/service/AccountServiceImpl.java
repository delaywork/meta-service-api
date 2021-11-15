package com.meta.service;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.meta.mapper.AccountMapper;
import com.meta.mapper.TenantMapper;
import com.meta.model.ErrorEnum;
import com.meta.model.FastRunTimeException;
import com.meta.model.TokenResponse;
import com.meta.model.WechatUtilLoginResponse;
import com.meta.model.pojo.Account;
import com.meta.model.pojo.Tenant;
import com.meta.model.request.IdRequest;
import com.meta.model.request.LoginByWechatCloudDTO;
import com.meta.model.request.LoginByWechatCloudRequest;
import com.meta.utils.*;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Log4j2
@Service
public class AccountServiceImpl {

    @Autowired
    private AccountMapper accountMapper;
    @Autowired
    private TenantMapper tenantMapper;
    @Autowired
    private WechatUtil wechatUtil;
    @Autowired
    private SmsUtil smsUtil;
    @Autowired
    private RedisUtil redisUtil;

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
            // 校验手机号是否被其他账号绑定
            QueryWrapper<Account> accountWrapper = new QueryWrapper<>();
            accountWrapper.lambda().eq(Account::getPhone, request.getPhone());
            Account phoneAccount = accountMapper.selectOne(accountWrapper);
            if (ObjectUtils.isEmpty(phoneAccount)){
                throw new FastRunTimeException(ErrorEnum.该手机号已被绑定其他账号);
            }
            // 验证短信验证码
            if (StringUtils.isEmpty(request.getPhone()) || StringUtils.isEmpty(request.getCode())){
                throw new FastRunTimeException(ErrorEnum.参数不正确);
            }
            String key = RedisKeys.SIGN_UP_SMS_CODE + request.getPhone() + ":" + request.getCode();
            if (!redisUtil.hasKey(key)){
                throw new FastRunTimeException(ErrorEnum.验证码验证失败);
            }
            // 清空历史发送的验证码
            redisUtil.deleteAll(RedisKeys.SIGN_UP_SMS_CODE + request.getPhone());
            // 创建Tenant
            Tenant tenant = Tenant.builder().build();
            tenantMapper.insert(tenant);
            // 创建Account
            account = Account.builder().openid(request.getOpenid()).unionid(request.getUnionid()).phone(request.getPhone()).name(request.getName()).wechatAuth(true).tenantId(tenant.getId()).build();
            accountMapper.insert(account);
        }
        tenantId = account.getTenantId();
        // 生成 token
        TokenResponse tokenResponse = JWTUtil.getAccessTokenAndRefreshToken(account.getId(), tenantId);
        return tokenResponse;
    }

    /**
     * 发送短信验证码
     * */
    public void sendSignUpSmsCode(String phone){
        String validateCode = CodeUtil.getNumberCode(6);
        smsUtil.singleSendMobileCode(phone, validateCode);
        String key = RedisKeys.SIGN_UP_SMS_CODE + phone + ":" + validateCode;
        redisUtil.setEx(key, validateCode, 5, TimeUnit.MINUTES);
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
