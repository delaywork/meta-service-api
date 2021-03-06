package com.meta.model.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;

public enum VerificationMethodEnum {
    PHONE("PHONE","手机"),
    EMAIL("EMAIL","邮箱"),
    PASSWORD("PASSWORD","密码"),
    ;

    @EnumValue
    private String value;

    private String describe;

    VerificationMethodEnum(String value, String describe){
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
