package com.meta.utils;

import com.meta.model.ErrorEnum;
import com.meta.model.FastRunTimeException;
import com.meta.model.enums.VerificationScenarioEnum;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Log4j2
@Component
public class SmsUtil {

    @Value("${SMS_API_KEY:}")
    private String SMS_API_KEY;
    @Value("${BINDING_SMS_TID:}")
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
                    params.add("tpl_value", URLEncoder.encode("#code#", StandardCharsets.UTF_8.name()) + "=" + URLEncoder.encode(code, StandardCharsets.UTF_8.name()));
                    break;
            }
            HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<MultiValueMap<String, String>>(params, headers);
            ResponseEntity<String> response = restTemplate.postForEntity(url, requestEntity , String.class);
            if (!HttpStatus.OK.equals(response.getStatusCode())){
                log.info("短信验证码发送失败，code:{}", response.getStatusCode());
                throw new FastRunTimeException(ErrorEnum.验证码发送失败);
            }
        }catch (Exception e){
            log.info("短信验证码发送失败，message:{}", e.getMessage());
            throw new FastRunTimeException(ErrorEnum.验证码发送失败);
        }
    }

}
