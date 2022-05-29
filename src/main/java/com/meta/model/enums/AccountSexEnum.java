package com.meta.model.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;

public enum AccountSexEnum {
    MALE("MALE","男"),
    WOMAN("WOMAN","女"),
    NEUTRAL("NEUTRAL","中性"),
    ;

    @EnumValue
    private String value;

    private String describe;

    AccountSexEnum(String value, String describe){
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
