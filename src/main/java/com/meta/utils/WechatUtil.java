package com.meta.utils;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import com.meta.model.ErrorEnum;
import com.meta.model.FastRunTimeException;
import com.meta.model.TokenResponse;
import com.meta.model.WechatUtilLoginResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Component
@Log4j2
public class WechatUtil {

    @Value("${WECHAT_APPID:}")
    private String WECHAT_APPID;
    @Value("${WECHAT_SECRET:}")
    private String WECHAT_SECRET;

    @Resource
    private RestTemplate restTemplate;

    public WechatUtilLoginResponse login(String jsCode) {
        String url = "https://api.weixin.qq.com/sns/jscode2session?appid={appid}&secret={secret}&js_code={code}&grant_type=authorization_code";
        Map<String, String> requestMap = new HashMap<>();
        requestMap.put("appid", WECHAT_APPID);
        requestMap.put("secret", WECHAT_SECRET);
        requestMap.put("code", jsCode);
        try{
            ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class,requestMap);
            JSONObject jsonObject = JSONObject.parseObject(responseEntity.getBody());
            if (ObjectUtil.isNotEmpty(jsonObject.getLong("errcode"))){
                Long errcode = jsonObject.getLong("errcode");
                if (errcode == 40029L){
                    log.info("wechat code无效: 40029");
                    throw new FastRunTimeException(ErrorEnum.code无效);
                }
                if (errcode == 45011L){
                    log.info("wechat 登录频率限制: 45011");
                    throw new FastRunTimeException(ErrorEnum.登录频率限制);
                }
                if (errcode == 40226){
                    log.info("wechat 高风险用户: 40226");
                    throw new FastRunTimeException(ErrorEnum.高风险用户);
                }
                if (errcode == 40164){
                    log.info("wechat white list exception: 40164");
                    throw new FastRunTimeException(ErrorEnum.微信白名单异常);
                }
            }
            return WechatUtilLoginResponse.builder().openid(jsonObject.getString("openid")).session_key(jsonObject.getString("session_key"))
                    .unionid(jsonObject.getString("unionid")).build();
        }catch (Exception e){
            throw new FastRunTimeException(ErrorEnum.登录失败);
        }
    }

}
