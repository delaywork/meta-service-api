package com.meta.model.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;

public enum MetaTermsTypeEnum {
    MEMBERS("MEMBERS","成员"),
    ;

    @EnumValue
    private String value;

    private String describe;

    MetaTermsTypeEnum(String value, String describe){
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
