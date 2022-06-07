package com.meta.model.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;

public enum ActivityDetailKeyEnum {
    NAME("NAME","名称"),
    AVATAR_URL("AVATAR_URL","头像"),
    TIME_ZONE("TIME_ZONE","时区"),
    TIME_ZONE_TEXT("TIME_ZONE_TEXT","时区描述"),
    LANGUAGE_TYPE("LANGUAGE_TYPE","多语言"),
    SEX("SEX","性别"),
    BIRTHDAY("BIRTHDAY","生日"),
    SECURITY_TYPE("SECURITY_TYPE","安全类型"),
    SECURITY_VALUE("SECURITY_VALUE","安全类型值"),
    ;

    @EnumValue
    private String value;

    private String describe;

    ActivityDetailKeyEnum(String value, String describe){
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
