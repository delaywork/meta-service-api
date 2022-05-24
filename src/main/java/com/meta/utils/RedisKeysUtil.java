package com.meta.utils;

public class RedisKeysUtil {

    public static String bindingPhoneLikeKey(String areaCode, String phone){
        return "BINDING_PHONE_SMS_CODE"+ areaCode + phone;
    }

    public static String bindingPhoneKey(String areaCode, String phone, String code){
        return "BINDING_PHONE_SMS_CODE"+ areaCode + phone + ":" + code;
    }


}
