package com.meta.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class UrlWhiteListUtil {

    @Value("${url.white.list:}")
    private String url_white;

    public List<String> urlWhiteList(){
        List<String> urlWhiteList = Arrays.asList(url_white.split(","));
        return urlWhiteList;
    }

}
