package com.meta.model.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;

public enum DocumentOperationTypeEnum {
    // ---------------------------------------------- Account --------------------------------------------------------
    DELETE("DELETE","删除"),
    MOVE("MOVE","移动"),

    // ---------------------------------------------- Document --------------------------------------------------------
    ;

    @EnumValue
    private String value;

    private String describe;

    DocumentOperationTypeEnum(String value, String describe){
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
