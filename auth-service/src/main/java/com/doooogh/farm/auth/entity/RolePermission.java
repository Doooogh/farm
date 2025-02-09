package com.doooogh.farm.auth.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 角色权限关联实体
 */
@Data
@TableName("sys_role_permission")
public class RolePermission {
    
    /**
     * 角色编码
     */
    private String roleCode;
    
    /**
     * 权限编码
     */
    private String permissionCode;
} 