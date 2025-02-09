package com.doooogh.farm.auth.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.doooogh.farm.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 角色实体
 */
@Data
@TableName("sys_role")
@EqualsAndHashCode(callSuper = true)
public class Role extends BaseEntity {
    
    /**
     * 角色编码
     */
    private String code;
    
    /**
     * 角色名称
     */
    private String name;
    
    /**
     * 角色描述
     */
    private String description;
    
    /**
     * 状态(0:禁用,1:正常)
     */
    private Integer status;
    
    /**
     * 排序
     */
    private Integer sort;
} 