package com.meta.model.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;

public enum DocumentTypeEnum {
    PDF("PDF","pdf"),
    FOLDER("FOLDER","文件夹"),
    ;

    @EnumValue
    private String value;

    private String describe;

    DocumentTypeEnum(String value, String describe){
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
