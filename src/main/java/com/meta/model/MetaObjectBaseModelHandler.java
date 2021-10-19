package com.meta.model;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

/**
 * @author Martin
 */
@Component
public class MetaObjectBaseModelHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        this.setFieldValByName("dataCreateTime", new Timestamp(System.currentTimeMillis()), metaObject);
        this.setFieldValByName("dataIsDeleted", false, metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.setFieldValByName("dataUpdateTime", new Timestamp(System.currentTimeMillis()), metaObject);
    }
}