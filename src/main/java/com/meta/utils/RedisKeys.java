package com.meta.utils;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;

public enum RedisKeys {
    TOKEN("token:","token"),
    SIGN_UP_SMS_CODE("signUpSmsCode:","注册手机验证码"),
    ;

    @EnumValue
    private String value;

    private String describe;

    RedisKeys(String value, String describe){
        this.value = value;
        this.describe = describe;
    }

    @JsonValue
    public String toValue() {
        return this.getValue();
    }

    public String getValue(){
        return value;
    }

    public String getDescribe(){
        return describe;
    }
}
