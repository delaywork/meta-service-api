package com.meta.model.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * 权限
 * */
public enum AuthorityEnum {
    INBOX("INBOX","inbox"),
    ALL("ALL","all"),
    ;

    @EnumValue
    private String value;

    private String describe;

    AuthorityEnum(String value, String describe){
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
