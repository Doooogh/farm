package com.doooogh.farm.user.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.doooogh.farm.common.exception.ServiceException;
import com.doooogh.farm.common.util.RedisUtil;
import com.doooogh.farm.user.entity.Permission;
import com.doooogh.farm.user.entity.RolePermission;
import com.doooogh.farm.user.mapper.PermissionMapper;
import com.doooogh.farm.user.mapper.RolePermissionMapper;
import com.doooogh.farm.user.model.PermissionTree;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author Li m13283354149@163.com
 * @date 2025/02/13
 * @description
 */
@Service
@Slf4j
@RequiredArgsConstructor

public class PermissionService extends ServiceImpl<PermissionMapper, Permission> {

    private final RolePermissionService rolePermissionService;
    private final RedisUtil redisUtil;

    /**
     * 创建权限
     */
    @Transactional(rollbackFor = Exception.class)
    public Permission createPermission(Permission permission) {
        // 检查权限编码是否存在
        Permission existingPermission = this.getOne(
                new LambdaQueryWrapper<Permission>().eq(Permission::getCode, permission.getCode())
        );
        if (existingPermission != null) {
            throw new ServiceException("权限编码已存在");
        }

        this.save(permission);
        clearPermissionCache();
        return permission;
    }

    /**
     * 更新权限
     */
    @Transactional(rollbackFor = Exception.class)
    public void updatePermission(Permission permission) {
        Permission existingPermission = this.getById(permission.getId());
        if (existingPermission == null) {
            throw new ServiceException("权限不存在");
        }

        // 不允许修改权限编码
        permission.setCode(existingPermission.getCode());

        this.updateById(permission);
        clearPermissionCache();
    }

    /**
     * 删除权限
     */
    @Transactional(rollbackFor = Exception.class)
    public void deletePermission(Long permissionId) {
        Permission permission = this.getById(permissionId);
        if (permission == null) {
            throw new ServiceException("权限不存在");
        }

        // 检查是否有子权限
        Long childCount = this.count(
                new LambdaQueryWrapper<Permission>().eq(Permission::getParentId, permissionId)
        );
        if (childCount > 0) {
            throw new ServiceException("存在子权限，无法删除");
        }

        // 删除角色-权限关联
        rolePermissionService.remove(
                new LambdaQueryWrapper<RolePermission>().eq(RolePermission::getPermissionCode, permission.getCode())
        );

        this.removeById(permissionId);
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
        List<Permission> permissions = this.list(
                new LambdaQueryWrapper<Permission>()
                        .eq(Permission::getStatus, 1)
                        .orderByAsc(Permission::getSort)
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
