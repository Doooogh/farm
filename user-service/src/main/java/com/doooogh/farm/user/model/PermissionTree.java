package com.doooogh.farm.user.model;

import com.doooogh.farm.user.entity.Permission;
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