package com.meta.model.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;

public enum DocumentCloudEnum {
    AZURE("AZURE","azure"),
    QIU("QIU","qiu"),
    ;

    @EnumValue
    private String value;

    private String describe;

    DocumentCloudEnum(String value, String describe){
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
