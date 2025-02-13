package com.doooogh.farm.user.controller;

import com.doooogh.farm.user.entity.Role;
import com.doooogh.farm.user.service.RoleService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RoleController {

    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping("/api/roles/find")
    public Role findRoleByCode(@RequestParam("code") String code) {
        return roleService.getOne(new QueryWrapper<Role>().eq("code", code));
    }
} 