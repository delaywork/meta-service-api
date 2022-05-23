package com.meta.model.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * 认证模式
 * */
public enum OauthGrantTypeEnum {
    PASSWORD("PASSWORD","password","账号密码模式"),
    REFRESH_TOKEN("REFRESH_TOKEN","refresh_token","刷新 token 验证"),
    WECHAT("WECHAT","wechat","微信模式"),
    AUTHORIZATION_CODE("AUTHORIZATION_CODE","authorization_code","授权码模式"),
    CLIENT_CREDENTIALS("CLIENT_CREDENTIALS","client_credentials","客户端模式"),
    IMPLICIT("IMPLICIT","implicit","简化模式"),
    ;

    @EnumValue
    private String value;

    private String oauthValue;

    private String describe;

    OauthGrantTypeEnum(String value,String oauthValue, String describe){
        this.value = value;
        this.oauthValue = oauthValue;
        this.describe = describe;
    }

    @JsonValue
    public String toValue() {
        return this.getValue();
    }

    public String getValue(){
        return value;
    }

    public String getOauthValue(){
        return oauthValue;
    }

    public String getDescribe(){
        return describe;
    }

}
