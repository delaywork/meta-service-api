package com.meta.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.meta.mapper.AccountMapper;
import com.meta.model.ErrorEnum;
import com.meta.model.FastRunTimeException;
import com.meta.model.pojo.Account;
import com.meta.model.request.AddVerificationRequest;
import com.meta.model.request.SecurityRequest;
import com.meta.utils.*;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@Service
@Log4j2
public class SecurityServiceImpl {

    @Resource
    private SmsUtil smsUtil;
    @Resource
    private RedisUtil redisUtil;
    @Resource
    private AccountMapper accountMapper;

    /**
     * 发送验证码
     * */
    public void addVerification(AddVerificationRequest request){
        if (ObjectUtils.isEmpty(request.getMethod()) || ObjectUtils.isEmpty(request.getScenario())){
            throw new FastRunTimeException(ErrorEnum.参数不正确);
        }
        // 判断发送类型
        switch (request.getMethod()){
            case PHONE:
                log.info("发送手机验证码");
                if (ObjectUtils.isEmpty(request.getAreaCode()) || ObjectUtils.isEmpty(request.getPhone())){
                    throw new FastRunTimeException(ErrorEnum.参数不正确);
                }
                String validateCode = CodeUtil.getNumberCode(6);
                smsUtil.singleSendMobileCode(request.getAreaCode(), request.getPhone(), request.getScenario(), validateCode);
                String redisKey = RedisKeysUtil.bindingPhoneKey(request.getAreaCode(), request.getPhone(), validateCode);
                redisUtil.setEx(redisKey, validateCode, 5, TimeUnit.MINUTES);
                break;
            case EMAIL:
                log.info("发送邮箱验证码");
                if (ObjectUtils.isEmpty(request.getEmail())){
                    throw new FastRunTimeException(ErrorEnum.参数不正确);
                }
                break;
        }
    }


    /**
     * 安全验证（绑定账户第三方验证端）
     * */
    public void security(SecurityRequest request){
        if (ObjectUtils.isEmpty(request.getMethod())){
            log.info("没有安全认证类型");
            throw new FastRunTimeException(ErrorEnum.参数不正确);
        }
        // 查询账号
        QueryWrapper<Account> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(Account::getId, request.getAccountId()).eq(Account::getDataIsDeleted, false);
        Account account = accountMapper.selectOne(wrapper);
        if (ObjectUtils.isEmpty(account)){
            throw new FastRunTimeException(ErrorEnum.帐户不存在);
        }
        switch (request.getMethod()){
            case PHONE:
                log.info("进行手机安全认证");
                // 校验手机号是否被其他账号绑定
                QueryWrapper<Account> accountWrapper = new QueryWrapper<>();
                accountWrapper.lambda().eq(Account::getPhone, request.getPhone()).ne(Account::getId, request.getAccountId()).eq(Account::getDataIsDeleted, false);
                Account phoneAccount = accountMapper.selectOne(accountWrapper);
                if (ObjectUtils.isEmpty(phoneAccount)){
                    throw new FastRunTimeException(ErrorEnum.该手机号已被绑定其他账号);
                }
                // 验证短信验证码
                if (StringUtils.isEmpty(request.getAreaCode()) || StringUtils.isEmpty(request.getPhone()) || StringUtils.isEmpty(request.getCode())){
                    throw new FastRunTimeException(ErrorEnum.参数不正确);
                }
                String redisKey = RedisKeysUtil.bindingPhoneKey(request.getAreaCode(), request.getPhone(), request.getCode());
                if (!redisUtil.hasKey(redisKey)){
                    throw new FastRunTimeException(ErrorEnum.验证码验证失败);
                }
                // 清空历史发送的验证码
                String likeKey = RedisKeysUtil.bindingPhoneLikeKey(request.getAreaCode(), request.getPhone());
                redisUtil.deleteAll(likeKey);
                // 绑定手机号
                UpdateWrapper<Account> updateWrapper = new UpdateWrapper<>();
                updateWrapper.lambda().eq(Account::getId, request.getAccountId()).eq(Account::getDataIsDeleted, false);
                updateWrapper.lambda().set(Account::getAreaCode, request.getAreaCode());
                updateWrapper.lambda().set(Account::getPhone, request.getPhone());
                accountMapper.update(Account.builder().build(), updateWrapper);
                break;
            case EMAIL:
                log.info("进行邮箱安全认证");
                break;
        }
    }

}
