package com.doooogh.farm.auth.client;

import com.doooogh.farm.common.auth.User;
import com.doooogh.farm.auth.model.AuthWechatUserInfo;
import com.doooogh.farm.common.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "user-service")
public interface UserServiceClient {

    @GetMapping("/api/users/find")
    Result<User> findUserByIdentifier(@RequestParam("identifier") String identifier);

    @PostMapping("/api/users/create")
    Result<User> createUserFromWechat(@RequestBody User user);
} 