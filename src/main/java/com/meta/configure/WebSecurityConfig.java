package com.meta.configure;

import com.meta.utils.UrlWhiteListUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import javax.annotation.Resource;
import java.util.List;

@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Resource
    private UrlWhiteListUtil urlWhiteListUtil;

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        List<String> urls = urlWhiteListUtil.urlWhiteList();
        http.authorizeRequests().antMatchers(urls.toArray(new String[]{})).permitAll()
                .anyRequest().authenticated().and()
                .httpBasic().and()
                .csrf().disable();
    }
}