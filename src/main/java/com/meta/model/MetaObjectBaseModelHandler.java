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
        this.setFieldValByName("dataCreateTime", System.currentTimeMillis(), metaObject);
        this.setFieldValByName("dataIsDeleted", false, metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.setFieldValByName("dataUpdateTime", System.currentTimeMillis(), metaObject);
    }
}