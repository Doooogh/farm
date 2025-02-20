package com.doooogh.farm.common.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * MyBatis-Plus自动填充处理器
 * 用于自动填充实体的创建时间、更新时间等字段
 */
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {

    /**
     * 插入时的填充策略
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        Date now = new Date();
        // 设置创建和更新时间
        this.strictInsertFill(metaObject, "createTime", Date.class, now);
        this.strictInsertFill(metaObject, "updateTime", Date.class, now);
        // 设置逻辑删除标志
        this.strictInsertFill(metaObject, "deleted", Boolean.class, false);
        // TODO: 从SecurityContext获取当前用户ID
        this.strictInsertFill(metaObject, "createBy", Long.class, 0L);
        this.strictInsertFill(metaObject, "updateBy", Long.class, 0L);
    }

    /**
     * 更新时的填充策略
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        // 更新修改时间
        this.strictUpdateFill(metaObject, "updateTime", Date.class, new Date());
        // TODO: 从SecurityContext获取当前用户ID
        this.strictUpdateFill(metaObject, "updateBy", Long.class, 0L);
    }
} 