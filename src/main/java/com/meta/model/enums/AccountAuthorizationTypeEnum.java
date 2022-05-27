package com.meta.model.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;

public enum AccountAuthorizationTypeEnum {
    TERMS_CONDITIONS("TERMS_CONDITIONS","条款"),
    WECHAT("WECHAT","微信"),
    ;

    @EnumValue
    private String value;

    private String describe;

    AccountAuthorizationTypeEnum(String value, String describe){
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
