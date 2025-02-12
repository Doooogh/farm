package com.doooogh.farm.auth.model;

import com.doooogh.farm.auth.entity.Permission;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class PermissionTree extends Permission {
    
    /**
     * 子权限列表
     */
    private List<PermissionTree> children;
} 