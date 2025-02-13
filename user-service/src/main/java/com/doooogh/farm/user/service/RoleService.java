package com.doooogh.farm.user.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.doooogh.farm.common.exception.BusinessException;
import com.doooogh.farm.common.exception.ServiceException;
import com.doooogh.farm.common.util.RedisUtil;
import com.doooogh.farm.user.entity.Role;
import com.doooogh.farm.user.entity.RolePermission;
import com.doooogh.farm.user.entity.UserRole;
import com.doooogh.farm.user.mapper.RoleMapper;
import com.doooogh.farm.user.mapper.RolePermissionMapper;
import com.doooogh.farm.user.mapper.UserRoleMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoleService extends ServiceImpl<RoleMapper, Role> implements IService<Role> {


    private final RolePermissionService rolePermissionService;
    private final RedisUtil redisUtil;

    private final UserRoleService userRoleService;

    /**
     * 创建角色
     *
     * @param role 角色信息
     * @return 创建的角色
     */
    @Transactional(rollbackFor = Exception.class)
    public Role createRole(Role role) {
        Role existingRole = this.getOne(
                new LambdaQueryWrapper<Role>().eq(Role::getCode, role.getCode())
        );
        if (existingRole != null) {
            throw BusinessException.alreadyExists("角色编码已存在");
        }

        // 保存角色
        this.save(role);

        // 清除角色缓存
        clearRoleCache();

        return role;
    }

    /**
     * 更新角色
     *
     * @param role 角色信息
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateRole(Role role) {
        Role existingRole = this.getById(role.getId());
        if (existingRole == null) {
            throw BusinessException.notFound("角色不存在");
        }

        // 不允许修改角色编码
        role.setCode(existingRole.getCode());

        // 更新角色
        this.updateById(role);

        // 清除角色缓存
        clearRoleCache();
    }

    /**
     * 删除角色
     *
     * @param roleId 角色ID
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteRole(Long roleId) {
        Role role = this.getById(roleId);
        if (role == null) {
            throw BusinessException.notFound("角色不存在");
        }

        Long userCount = userRoleService.count(
                new LambdaQueryWrapper<UserRole>().eq(UserRole::getRoleCode, role.getCode())
        );
        if (userCount > 0) {
            throw BusinessException.operationDenied("该角色下存在用户，无法删除");
        }

        // 删除角色权限关联
        rolePermissionService.remove(
                new LambdaQueryWrapper<RolePermission>().eq(RolePermission::getRoleCode, role.getCode())
        );

        // 删除角色
        this.removeById(roleId);

        // 清除角色缓存
        clearRoleCache();
    }

    /**
     * 分配角色权限
     *
     * @param roleCode 角色编码
     * @param permissionCodes 权限编码列表
     */
    @Transactional(rollbackFor = Exception.class)
    public void assignPermissions(String roleCode, List<String> permissionCodes) {
        // 检查角色是否存在
        Role role =this.getOne(
                new LambdaQueryWrapper<Role>().eq(Role::getCode, roleCode)
        );
        if (role == null) {
            throw new ServiceException("角色不存在");
        }

        // 删除原有权限
        rolePermissionService.remove(
                new LambdaQueryWrapper<RolePermission>().eq(RolePermission::getRoleCode, role.getCode())
        );

        // 如果权限列表不为空，则插入新的权限
        if (!CollectionUtils.isEmpty(permissionCodes)) {
            // 逐个插入新的权限
            for (String permissionCode : permissionCodes) {
                RolePermission rolePermission = new RolePermission();
                rolePermission.setRoleCode(role.getCode());
                rolePermission.setPermissionCode(permissionCode);
                rolePermissionService.save(rolePermission);
            }
        }

        // 清除角色权限缓存
        clearRoleCache();
    }

    /**
     * 获取所有角色
     *
     * @return 角色列表
     */
    public List<Role> getAllRoles() {
        return this.list(
                new LambdaQueryWrapper<Role>()
                        .eq(Role::getStatus, 1)
                        .orderByAsc(Role::getSort)
        );
    }

    /**
     * 获取角色权限
     *
     * @param roleCode 角色编码
     * @return 权限编码列表
     */
    public List<String> getRolePermissions(String roleCode) {
        // 从缓存获取
        String cacheKey = "role:permissions:" + roleCode;
        @SuppressWarnings("unchecked")
        List<String> permissions = (List<String>) redisUtil.get(cacheKey);
        if (permissions != null) {
            return permissions;
        }

        // 查询数据库
        permissions = rolePermissionService.list(
                        new LambdaQueryWrapper<RolePermission>()
                                .eq(RolePermission::getRoleCode, roleCode)
                ).stream()
                .map(RolePermission::getPermissionCode)
                .collect(Collectors.toList());

        // 缓存结果
        if (!permissions.isEmpty()) {
            redisUtil.set(cacheKey, permissions, 1, TimeUnit.HOURS);
        }

        return permissions;
    }

    /**
     * 清除角色相关缓存
     */
    private void clearRoleCache() {
        redisUtil.delete("role:permissions:*");
    }
} 