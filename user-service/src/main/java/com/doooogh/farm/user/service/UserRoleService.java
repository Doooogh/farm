package com.doooogh.farm.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.doooogh.farm.user.entity.UserRole;
import com.doooogh.farm.user.mapper.UserRoleMapper;
import org.springframework.stereotype.Service;

@Service
public class UserRoleService extends ServiceImpl<UserRoleMapper, UserRole>  {
    // 在这里实现与用户角色相关的业务逻辑
} 