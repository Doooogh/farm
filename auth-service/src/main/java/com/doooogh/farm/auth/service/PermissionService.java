package com.doooogh.farm.auth.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.doooogh.farm.auth.entity.Permission;
import com.doooogh.farm.auth.entity.RolePermission;
import com.doooogh.farm.auth.exception.ServiceException;
import com.doooogh.farm.auth.mapper.PermissionMapper;
import com.doooogh.farm.auth.mapper.RolePermissionMapper;
import com.doooogh.farm.auth.model.PermissionTree;
import com.doooogh.farm.common.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 权限服务
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class PermissionService {
    
    private final PermissionMapper permissionMapper;
    private final RolePermissionMapper rolePermissionMapper;
    private final RedisUtil redisUtil;
    
    /**
     * 创建权限
     */
    @Transactional(rollbackFor = Exception.class)
    public Permission createPermission(Permission permission) {
        // 检查权限编码是否存在
        Permission existingPermission = permissionMapper.selectOne(
            new QueryWrapper<Permission>().eq("code", permission.getCode())
        );
        if (existingPermission != null) {
            throw new ServiceException("权限编码已存在");
        }
        
        permissionMapper.insert(permission);
        clearPermissionCache();
        return permission;
    }
    
    /**
     * 更新权限
     */
    @Transactional(rollbackFor = Exception.class)
    public void updatePermission(Permission permission) {
        Permission existingPermission = permissionMapper.selectById(permission.getId());
        if (existingPermission == null) {
            throw new ServiceException("权限不存在");
        }
        
        // 不允许修改权限编码
        permission.setCode(existingPermission.getCode());
        
        permissionMapper.updateById(permission);
        clearPermissionCache();
    }
    
    /**
     * 删除权限
     */
    @Transactional(rollbackFor = Exception.class)
    public void deletePermission(Long permissionId) {
        Permission permission = permissionMapper.selectById(permissionId);
        if (permission == null) {
            throw new ServiceException("权限不存在");
        }
        
        // 检查是否有子权限
        Long childCount = permissionMapper.selectCount(
            new QueryWrapper<Permission>().eq("parent_id", permissionId)
        );
        if (childCount > 0) {
            throw new ServiceException("存在子权限，无法删除");
        }
        
        // 删除角色-权限关联
        rolePermissionMapper.delete(
            new QueryWrapper<RolePermission>().eq("permission_code", permission.getCode())
        );
        
        permissionMapper.deleteById(permissionId);
        clearPermissionCache();
    }
    
    /**
     * 获取权限树
     */
    public List<PermissionTree> getPermissionTree() {
        // 从缓存获取
        String cacheKey = "permission:tree";
        List<PermissionTree> tree = (List<PermissionTree>) redisUtil.get(cacheKey);
        if (tree != null) {
            return tree;
        }
        
        // 查询所有权限
        List<Permission> permissions = permissionMapper.selectList(
            new QueryWrapper<Permission>()
                .eq("status", 1)
                .orderByAsc("sort")
        );
        
        // 构建树形结构
        tree = buildPermissionTree(permissions, 0L);
        
        // 缓存结果
        redisUtil.set(cacheKey, tree, 1, TimeUnit.HOURS);
        
        return tree;
    }
    
    /**
     * 构建权限树
     */
    private List<PermissionTree> buildPermissionTree(List<Permission> permissions, Long parentId) {
        return permissions.stream()
            .filter(p -> parentId.equals(p.getParentId()))
            .map(p -> {
                PermissionTree node = new PermissionTree();
                BeanUtils.copyProperties(p, node);
                List<PermissionTree> children = buildPermissionTree(permissions, p.getId());
                if (!children.isEmpty()) {
                    node.setChildren(children);
                }
                return node;
            })
            .collect(Collectors.toList());
    }
    
    /**
     * 清除权限缓存
     */
    private void clearPermissionCache() {
        redisUtil.delete("permission:tree");
        redisUtil.delete("role:permissions:*");
    }
} 