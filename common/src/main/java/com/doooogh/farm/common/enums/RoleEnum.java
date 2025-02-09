package com.doooogh.farm.common.enums;

import lombok.Getter;

/**
 * 角色枚举
 * 定义系统中的所有角色
 */
@Getter
public enum RoleEnum {
    
    ADMIN("ROLE_ADMIN", "管理员"),
    USER("ROLE_USER", "普通用户");
    
    private final String code;
    private final String desc;
    
    RoleEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
    
    public String getCode() {
        return code;
    }
    
    public String getDesc() {
        return desc;
    }
} 