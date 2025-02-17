package com.doooogh.farm.auth.client;

import com.doooogh.farm.common.auth.Role;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "user-role-service")
public interface RoleServiceClient {

    @GetMapping("/api/roles/find")
    Role findRoleByCode(@RequestParam("code") String code);
} 