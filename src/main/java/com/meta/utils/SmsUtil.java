package com.meta.utils;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import com.meta.model.ErrorEnum;
import com.meta.model.FastRunTimeException;
import com.meta.model.WechatUtilLoginResponse;
import com.meta.model.enums.VerificationScenarioEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

@Component
public class SmsUtil {

    @Value("${SMS_API_KEY:}")
    private String SMS_API_KEY;
    @Value("${BINDING_SMS_TID:4889388}")
    private String BINDING_SMS_TID;

    @Autowired
    private RestTemplate restTemplate;

    /**
     * 单个发送手机验证码
     * */
    public void singleSendMobileCode(String areaCode, String phone, VerificationScenarioEnum scenario, String code) {
        String url = "https://sms.yunpian.com/v2/sms/tpl_single_send.json";
        try{
            HttpHeaders headers = new HttpHeaders();
            MediaType type = MediaType.parseMediaType("application/x-www-form-urlencoded; charset=utf-8");
            headers.setContentType(type);
            headers.add("Accept", MediaType.APPLICATION_JSON.toString());
            MultiValueMap<String, String> params= new LinkedMultiValueMap<String, String>();
            params.add("apikey", SMS_API_KEY);
            params.add("mobile", phone);
            switch (scenario){
                case BINDING:
                    params.add("tpl_id", BINDING_SMS_TID);
                    params.add("tpl_value", URLEncoder.encode("#code#") + "=" + URLEncoder.encode(code));
                    break;
            }
            HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<MultiValueMap<String, String>>(params, headers);
            ResponseEntity<String> response = restTemplate.postForEntity(url, requestEntity , String.class);
            if (!HttpStatus.OK.equals(response.getStatusCode())){
                throw new FastRunTimeException(ErrorEnum.验证码发送失败);
            }
        }catch (Exception e){
            throw new FastRunTimeException(ErrorEnum.验证码发送失败);
        }
    }

}
