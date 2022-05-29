package com.meta.configure;

import com.meta.filter.TokenFilter;
import com.meta.model.enums.AuthorityEnum;
import com.meta.utils.UrlWhiteListUtil;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.annotation.Resource;
import java.util.List;

@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    @Resource
    private TokenStore tokenStore;
    @Resource
    private TokenFilter tokenFilter;
    @Resource
    private ResourceTokenExceptionEntryPoint resourceTokenExceptionEntryPoint;
    @Resource
    private UrlWhiteListUtil urlWhiteListUtil;

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
        resources.stateless(true).tokenStore(tokenStore).authenticationEntryPoint(resourceTokenExceptionEntryPoint);
    }

    // 配置 URL 访问权限
    @Override
    public void configure(HttpSecurity http) throws Exception {
        List<String> urls = urlWhiteListUtil.urlWhiteList();
        http.authorizeRequests()
                .antMatchers(urls.toArray(new String[]{})).permitAll()
                .antMatchers("/users/**","/info").hasAnyAuthority(AuthorityEnum.INBOX.getValue(), AuthorityEnum.ALL.getValue())
                .antMatchers("/**").hasAuthority(AuthorityEnum.ALL.getValue())
                .anyRequest().authenticated().and().httpBasic().and().csrf().disable();
        http.addFilterBefore(tokenFilter, BasicAuthenticationFilter.class);
    }
}