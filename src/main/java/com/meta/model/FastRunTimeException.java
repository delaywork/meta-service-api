package com.meta.model;

import lombok.Data;

@Data
public class FastRunTimeException extends RuntimeException {

    private String code = "-1";
    private String message ;

    public FastRunTimeException(String message) {
        this.message = message;
    }

    public FastRunTimeException(ErrorEnum errorEnum) {
        this.message = errorEnum.message;
        this.code = errorEnum.code;
    }

    public FastRunTimeException(ErrorEnum errorEnum, String param1) {
        String param1Message = errorEnum.message.replace("{}", param1);
        this.message = param1Message;
        this.code = errorEnum.code;
    }

    public FastRunTimeException(ErrorEnum errorEnum, String param1, String param2) {
        String param1Message = errorEnum.message.replace("{}", param1);
        String param2Message = param1Message.replace("{}", param2);
        this.message = param2Message;
        this.code = errorEnum.code;
    }

    public FastRunTimeException(ErrorEnum errorEnum, String param1, String param2, String param3) {
        String param1Message = errorEnum.message.replace("{}", param1);
        String param2Message = param1Message.replace("{}", param2);
        String param3Message = param2Message.replace("{}", param3);
        this.message = param3Message;
        this.code = errorEnum.code;
    }

    public FastRunTimeException(ErrorEnum errorEnum, String param1, String param2, String param3, String param4) {
        String param1Message = errorEnum.message.replace("{}", param1);
        String param2Message = param1Message.replace("{}", param2);
        String param3Message = param2Message.replace("{}", param3);
        String param4Message = param3Message.replace("{}", param4);
        this.message = param4Message;
        this.code = errorEnum.code;
    }

    public FastRunTimeException(ErrorEnum errorEnum, String param1, String param2, String param3, String param4, String param5) {
        String param1Message = errorEnum.message.replace("{}", param1);
        String param2Message = param1Message.replace("{}", param2);
        String param3Message = param2Message.replace("{}", param3);
        String param4Message = param3Message.replace("{}", param4);
        String param5Message = param4Message.replace("{}", param5);
        this.message = param5Message;
        this.code = errorEnum.code;
    }

    public FastRunTimeException(String code, String message) {
        this.message = message;
        this.code = code;
    }

}
