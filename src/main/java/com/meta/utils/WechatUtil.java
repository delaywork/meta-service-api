package com.meta.utils;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.api.IErrorCode;
import com.meta.model.ErrorEnum;
import com.meta.model.FastRunTimeException;
import com.meta.model.TokenResponse;
import com.meta.model.WechatUtilLoginResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Component
public class WechatUtil {

    @Value("${wechat.appid:}")
    private String appid;
    @Value("${wechat.secret:}")
    private String secret;

    @Autowired
    private RestTemplate restTemplate;

    public WechatUtilLoginResponse login(String jsCode) {
        String url = "https://api.weixin.qq.com/sns/jscode2session?appid={appid}&secret={secret}&js_code={code}&grant_type=authorization_code";
        Map<String, String> requestMap = new HashMap<>();
        requestMap.put("appid", appid);
        requestMap.put("secret", secret);
        requestMap.put("code", jsCode);
        try{
            ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class,requestMap);
            JSONObject jsonObject = JSONObject.parseObject(responseEntity.getBody());
            if (ObjectUtil.isNotEmpty(jsonObject.getLong("errcode"))){
                Long errcode = jsonObject.getLong("errcode");
                if (errcode == 40029L){
                    throw new FastRunTimeException(ErrorEnum.code无效);
                }
                if (errcode == 45011L){
                    throw new FastRunTimeException(ErrorEnum.登录频率限制);
                }
                if (errcode == 40226){
                    throw new FastRunTimeException(ErrorEnum.高风险用户);
                }
            }
            return WechatUtilLoginResponse.builder().openid(jsonObject.getString("openid")).session_key(jsonObject.getString("session_key"))
                    .unionid(jsonObject.getString("unionid")).build();
        }catch (Exception e){
            throw new FastRunTimeException(ErrorEnum.登录失败);
        }
    }

}
