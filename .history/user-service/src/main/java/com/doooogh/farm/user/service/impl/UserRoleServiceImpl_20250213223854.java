package com.doooogh.farm.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.doooogh.farm.user.entity.UserRole;
import com.doooogh.farm.user.mapper.UserRoleMapper;
import com.doooogh.farm.user.service.UserRoleService;
import org.springframework.stereotype.Service;

@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements UserRoleService {
    // 在这里实现与用户角色相关的业务逻辑
} 