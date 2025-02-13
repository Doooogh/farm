package com.doooogh.farm.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.doooogh.farm.user.entity.UserRole;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserRoleMapper extends BaseMapper<UserRole> {
    // 在这里定义与用户角色相关的数据库操作
} 