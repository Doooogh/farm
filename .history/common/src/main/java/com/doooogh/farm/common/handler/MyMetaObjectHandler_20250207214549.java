package com.doooogh.farm.common.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class MyMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        this.strictInsertFill(metaObject, "createTime", LocalDateTime.class, LocalDateTime.now());
        this.strictInsertFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());
        this.strictInsertFill(metaObject, "deleted", Boolean.class, false);
        // TODO: 从SecurityContext获取当前用户ID
        this.strictInsertFill(metaObject, "createBy", Long.class, 0L);
        this.strictInsertFill(metaObject, "updateBy", Long.class, 0L);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.strictUpdateFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());
        // TODO: 从SecurityContext获取当前用户ID
        this.strictUpdateFill(metaObject, "updateBy", Long.class, 0L);
    }
} 