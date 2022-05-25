package com.meta.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class UrlWhiteListUtil {

    @Value("${WHITE_LIST_URL:}")
    private String WHITE_LIST_URL;

    public List<String> urlWhiteList(){
        List<String> urlWhiteList = Arrays.asList(WHITE_LIST_URL.split(","));
        return urlWhiteList;
    }

}
