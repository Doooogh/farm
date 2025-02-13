package com.doooogh.farm.user.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.doooogh.farm.user.entity.RolePermission;
import com.doooogh.farm.user.mapper.RolePermissionMapper;
import io.swagger.v3.oas.annotations.servers.Server;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author Li m13283354149@163.com
 * @date 2025/02/13
 * @description
 */
@Service
@Slf4j
public class RolePermissionService extends ServiceImpl<RolePermissionMapper, RolePermission> {
}
