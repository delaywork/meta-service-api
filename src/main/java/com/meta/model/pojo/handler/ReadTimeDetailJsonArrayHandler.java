package com.meta.model.pojo.handler;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.handlers.FastjsonTypeHandler;
import com.meta.model.pojo.ReadTimeDetail;

public class ReadTimeDetailJsonArrayHandler extends FastjsonTypeHandler {

    private final Class<? extends Object> type;

    public ReadTimeDetailJsonArrayHandler(Class<?> type) {
        super(type);
        this.type = type;
    }

    @Override
    protected Object parse(String json) {
        return JSON.parseArray(json, ReadTimeDetail.class);
    }

    @Override
    protected String toJson(Object obj) {
        return super.toJson(obj);
    }

}
