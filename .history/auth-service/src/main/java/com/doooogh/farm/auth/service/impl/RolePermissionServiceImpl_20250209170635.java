package com.doooogh.farm.auth.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.doooogh.farm.auth.entity.RolePermission;
import com.doooogh.farm.auth.mapper.RolePermissionMapper;
import com.doooogh.farm.auth.service.IRolePermissionService;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class RolePermissionServiceImpl extends ServiceImpl<RolePermissionMapper, RolePermission> 
    implements IRolePermissionService {
    
    public boolean saveBatch(Collection<RolePermission> entityList) {
        return super.saveBatch(entityList);
    }
} 