package com.meta.utils;

import com.meta.model.enums.VerificationMethodEnum;
import com.meta.model.enums.VerificationScenarioEnum;

public class RedisKeysUtil {

    /**
     * 绑定手机模糊 key
     * */
    public static String bindingPhoneLikeKey(String areaCode, String phone){
        return "BINDING_PHONE_SMS_CODE"+ areaCode + phone;
    }

    /**
     * 绑定手机 key
     * */
    public static String bindingPhoneKey(String areaCode, String phone, String code){
        return "BINDING_PHONE_SMS_CODE"+ areaCode + phone + ":" + code;
    }

    /**
     * 验证码倒计时
     */
    public static String sendVerificationCodeCountdown(VerificationMethodEnum verificationMethod, VerificationScenarioEnum verificationScenario, String value){
        return "SEND_VERIFICATION_CODE_COUNTDOWN:" + verificationMethod.getValue() + ":" + verificationScenario.getValue() + ":" + value;
    }


}
