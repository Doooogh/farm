package com.doooogh.farm.user.controller;

import com.doooogh.farm.user.entity.User;
import com.doooogh.farm.user.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
} 