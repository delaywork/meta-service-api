package com.meta.model.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;

public enum ActivityOperationTypeEnum {
    AGREE_TO_WECHAT_AUTHORIZATION("AGREE_TO_WECHAT_AUTHORIZATION","同意微信授权"),
    AGREE_TO_TERMS_CONDITIONS("AGREE_TO_TERMS_CONDITIONS","同意条款"),
    AGREE_TO_POLICY("AGREE_TO_POLICY","同意策略"),
    ;

    @EnumValue
    private String value;

    private String describe;

    ActivityOperationTypeEnum(String value, String describe){
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
