package com.doooogh.farm.user.controller;

import com.doooogh.farm.user.entity.User;
import com.doooogh.farm.user.service.UserService;
import com.doooogh.farm.auth.model.AuthWechatUserInfo;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/api/users/find")
    public User findUserByIdentifier(@RequestParam("identifier") String identifier) {
        return userService.findUserByIdentifier(identifier);
    }

    @PostMapping("/api/users/create")
    public User createUserFromWechat(@RequestBody AuthWechatUserInfo wechatUserInfo) {
        return userService.createUserFromWechat(wechatUserInfo);
    }
} 