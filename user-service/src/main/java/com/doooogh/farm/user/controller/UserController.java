package com.doooogh.farm.user.controller;

import com.doooogh.farm.common.result.Result;
import com.doooogh.farm.user.entity.User;
import com.doooogh.farm.user.model.UserDTO;
import com.doooogh.farm.user.service.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/api/users/find")
    public Result<User> findUserByIdentifier(@RequestParam("identifier") String identifier) {
        return Result.ok(userService.findUserByIdentifier(identifier));
    }

    @PostMapping("/api/users/create")
    public Result<UserDTO> createUserFromWechat(@RequestBody User user) {
        return Result.ok(userService.createUser(user));
    }
} 