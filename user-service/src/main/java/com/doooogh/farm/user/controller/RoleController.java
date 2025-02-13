package com.doooogh.farm.user.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.doooogh.farm.common.result.Result;
import com.doooogh.farm.user.entity.Role;
import com.doooogh.farm.user.service.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class RoleController {

    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping("/api/roles/find")
    public Role findRoleByCode(@RequestParam("code") String code) {
        return roleService.getOne(new LambdaQueryWrapper<Role>().eq(Role::getCode, code));
    }

    @PostMapping
    @Operation(summary = "创建角色")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Role> createRole(@RequestBody @Valid Role role) {
        return Result.ok(roleService.createRole(role));
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新角色")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Void> updateRole(@PathVariable Long id, @RequestBody @Valid Role role) {
        role.setId(id);
        roleService.updateRole(role);
        return Result.ok();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除角色")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Void> deleteRole(@PathVariable Long id) {
        roleService.deleteRole(id);
        return Result.ok();
    }

    @PostMapping("/{roleCode}/permissions")
    @Operation(summary = "分配角色权限")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Void> assignPermissions(
            @PathVariable String roleCode,
            @RequestBody List<String> permissionCodes
    ) {
        roleService.assignPermissions(roleCode, permissionCodes);
        return Result.ok();
    }

    @GetMapping
    @Operation(summary = "获取所有角色")
    public Result<List<Role>> getAllRoles() {
        return Result.ok(roleService.getAllRoles());
    }

    @GetMapping("/{roleCode}/permissions")
    @Operation(summary = "获取角色权限")
    public Result<List<String>> getRolePermissions(@PathVariable String roleCode) {
        return Result.ok(roleService.getRolePermissions(roleCode));
    }
} 