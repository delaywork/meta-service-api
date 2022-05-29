package com.meta.model.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;

public enum InfoTermsTypeEnum {
    ABOUT("ABOUT","关于"),
    TERMS_CONDITIONS("TERMS_CONDITIONS","条款"),
    POLICY("POLICY","政策"),
    ;

    @EnumValue
    private String value;

    private String describe;

    InfoTermsTypeEnum(String value, String describe){
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
