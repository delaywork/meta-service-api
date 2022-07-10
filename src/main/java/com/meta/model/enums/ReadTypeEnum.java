package com.meta.model.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;

public enum ReadTypeEnum {
    ORIGINAL("ORIGINAL","原始版本"),
    WATERMARK("WATERMARK","水印版本"),
    ;

    @EnumValue
    private String value;

    private String describe;

    ReadTypeEnum(String value, String describe){
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
