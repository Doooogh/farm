package com.doooogh.farm.auth.client;

import com.doooogh.farm.auth.entity.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "user-service")
public interface UserServiceClient {

    @GetMapping("/api/users/find")
    User findUserByIdentifier(@RequestParam("identifier") String identifier);
} 