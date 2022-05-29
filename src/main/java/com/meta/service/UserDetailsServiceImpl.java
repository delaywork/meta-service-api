package com.meta.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.meta.mapper.AccountMapper;
import com.meta.model.enums.AuthorityEnum;
import com.meta.model.pojo.Account;
import com.meta.model.request.vo.UserOauthVo;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Log4j2
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Resource
    private AccountMapper accountMapper;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        log.info("UserDetailsServiceImpl ---> loadUserByUsername, s:{}", s);
        // 获取目标账号信息
        QueryWrapper<Account> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Account::getId, s);
        queryWrapper.lambda().eq(Account::getDataIsDeleted, false);
        queryWrapper.last("limit 1");
        Account account = accountMapper.selectOne(queryWrapper);

        // 生成认证账号信息
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        if (ObjectUtils.isNotEmpty(account.getAreaCode()) && ObjectUtils.isNotEmpty(account.getPhone())){
            log.info("微信关联账号已经完成手机因子绑定，accountId:{}, areaCode:{}, phone:{}", account.getId(), account.getAreaCode(), account.getPhone());
            authorities.add(new SimpleGrantedAuthority(AuthorityEnum.ALL.getValue()));
        }else{
            log.info("微信关联账号未完成手机因子绑定，accountId:{}", account.getId());
            authorities.add(new SimpleGrantedAuthority(AuthorityEnum.INBOX.getValue()));
        }
        UserOauthVo user = new UserOauthVo(account.getId(), account.getName(), account.getId().toString(), account.getPassword(), true, authorities);
        return user;
    }
}
