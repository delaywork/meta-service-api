package com.meta.model.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;

public enum ActivityOperationResourceEnum {
    ACCOUNT("ACCOUNT","账号"),
    DOCUMENT("DOCUMENT","文件"),
    OPPORTUNITY("OPPORTUNITY","分享"),
    ;

    @EnumValue
    private String value;

    private String describe;

    ActivityOperationResourceEnum(String value, String describe){
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
