package com.meta.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class ReturnData<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    private static final String SUCCESS = "SUCCESS";

    private T data;
    private String code;
    private String msg;

    private ReturnData(String code, T data, String msg) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static final ReturnData success() {
        return success(null);
    }

    public static final ReturnData success(Object data) {
        return new ReturnData(SUCCESS, data, "Success.");
    }

    public static final ReturnData failed(FastRunTimeException exception){
        return new ReturnData(exception.getCode(), null, exception.getMessage());
    }

    public static final ReturnData failed(FastRunTimeException exception, Object data){
        return new ReturnData(exception.getCode(), data, exception.getMessage());
    }

}
