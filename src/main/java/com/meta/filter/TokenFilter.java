package com.meta.filter;

import com.alibaba.fastjson.JSONObject;
import com.meta.model.ErrorEnum;
import com.meta.model.FastRunTimeException;
import com.meta.model.ReturnData;
import com.meta.utils.UrlWhiteListUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * token 拦截器（判断请求是否都带有 token）
 * */
@Component
@Log4j2
public class TokenFilter extends OncePerRequestFilter {

    @Resource
    private UrlWhiteListUtil urlWhiteListUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String uri=request.getRequestURI();
        boolean present = urlWhiteListUtil.urlWhiteList().stream().filter(url -> url.equals(uri)).findAny().isPresent();
        if(present){
            log.info("接口 {} 在白名单中", uri);
            filterChain.doFilter(request,response);
        }else{
            log.info("接口 {} 不在白名单中", uri);
            boolean access_token=false;
            boolean authorization=false;
            if(request.getParameter("access_token")==null){
                log.info("接口 {} 未携带 access_token", uri);
                access_token=true;
            }
            if(request.getHeader("Authorization")==null){
                log.info("接口 {} 未携带 Authorization", uri);
                authorization=true;
            }else{
                if(!request.getHeader("Authorization").startsWith("Bearer") && !request.getHeader("Authorization").startsWith("bearer")){
                    log.info("接口 {} Authorization 不是 Bearer 开头", uri);
                    authorization=true;
                }
            }
            if(access_token&&authorization){
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write(JSONObject.toJSONString(ReturnData.failed(new FastRunTimeException(ErrorEnum.token异常))));
            }else{
                filterChain.doFilter(request,response);
            }
        }
    }
}
