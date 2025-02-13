package com.doooogh.farm.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.doooogh.farm.user.entity.Role;
import com.doooogh.farm.user.mapper.RoleMapper;
import com.doooogh.farm.user.service.RoleService;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {
    // 实现与角色相关的业务逻辑
} 