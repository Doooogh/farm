package com.doooogh.farm.auth.controller;

import com.doooogh.farm.auth.entity.Role;
import com.doooogh.farm.auth.service.RoleService;
import com.doooogh.farm.common.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 角色管理控制器
 */
@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
@Tag(name = "角色管理", description = "角色相关接口")
public class RoleController {
    
    private final RoleService roleService;
    
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