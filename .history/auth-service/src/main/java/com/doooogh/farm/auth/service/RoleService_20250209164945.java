package com.doooogh.farm.auth.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.doooogh.farm.auth.entity.Role;
import com.doooogh.farm.auth.entity.RolePermission;
import com.doooogh.farm.auth.entity.UserRole;
import com.doooogh.farm.auth.exception.ServiceException;
import com.doooogh.farm.auth.mapper.RoleMapper;
import com.doooogh.farm.auth.mapper.RolePermissionMapper;
import com.doooogh.farm.auth.mapper.UserRoleMapper;
import com.doooogh.farm.common.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 角色服务
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class RoleService {
    
    private final RoleMapper roleMapper;
    private final RolePermissionMapper rolePermissionMapper;
    private final RedisUtil redisUtil;
    
    /**
     * 创建角色
     *
     * @param role 角色信息
     * @return 创建的角色
     */
    @Transactional(rollbackFor = Exception.class)
    public Role createRole(Role role) {
        // 检查角色编码是否存在
        Role existingRole = roleMapper.selectOne(
            new QueryWrapper<Role>().eq("code", role.getCode())
        );
        if (existingRole != null) {
            throw new ServiceException("角色编码已存在");
        }
        
        // 保存角色
        roleMapper.insert(role);
        
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
        // 检查角色是否存在
        Role existingRole = roleMapper.selectById(role.getId());
        if (existingRole == null) {
            throw new ServiceException("角色不存在");
        }
        
        // 不允许修改角色编码
        role.setCode(existingRole.getCode());
        
        // 更新角色
        roleMapper.updateById(role);
        
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
        // 检查角色是否存在
        Role role = roleMapper.selectById(roleId);
        if (role == null) {
            throw new ServiceException("角色不存在");
        }
        
        // 检查是否有用户关联此角色
        Integer userCount = userRoleMapper.selectCount(
            new QueryWrapper<UserRole>().eq("role_code", role.getCode())
        );
        if (userCount > 0) {
            throw new ServiceException("该角色下存在用户，无法删除");
        }
        
        // 删除角色权限关联
        rolePermissionMapper.delete(
            new QueryWrapper<RolePermission>().eq("role_code", role.getCode())
        );
        
        // 删除角色
        roleMapper.deleteById(roleId);
        
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
        // 删除原有权限
        rolePermissionMapper.delete(
            new QueryWrapper<RolePermission>().eq("role_code", roleCode)
        );
        
        // 批量插入新权限
        if (!CollectionUtils.isEmpty(permissionCodes)) {
            List<RolePermission> rolePermissions = permissionCodes.stream()
                .map(permissionCode -> {
                    RolePermission rp = new RolePermission();
                    rp.setRoleCode(roleCode);
                    rp.setPermissionCode(permissionCode);
                    return rp;
                })
                .collect(Collectors.toList());
            rolePermissionMapper.insertBatch(rolePermissions);
        }
        
        // 清除角色缓存
        clearRoleCache();
    }
    
    /**
     * 获取所有角色
     *
     * @return 角色列表
     */
    public List<Role> getAllRoles() {
        return roleMapper.selectList(new QueryWrapper<Role>()
            .eq("status", 1)
            .orderByAsc("sort"));
    }
    
    /**
     * 获取角色权限
     *
     * @param roleCode 角色编码
     * @return 权限编码列表
     */
    public List<String> getRolePermissions(String roleCode) {
        List<RolePermission> rolePermissions = rolePermissionMapper.selectList(
            new QueryWrapper<RolePermission>().eq("role_code", roleCode)
        );
        return rolePermissions.stream()
            .map(RolePermission::getPermissionCode)
            .collect(Collectors.toList());
    }
    
    /**
     * 清除角色缓存
     */
    private void clearRoleCache() {
        redisUtil.delete("role:all");
        redisUtil.delete("role:permissions:*");
    }
} 