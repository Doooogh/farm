package com.doooogh.farm.auth.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 用户角色关联实体
 */
@Data
@TableName("sys_user_role")
public class UserRole {
    
    /**
     * 用户ID
     */
    private Long userId;
    
    /**
     * 角色编码
     */
    private String roleCode;
} 