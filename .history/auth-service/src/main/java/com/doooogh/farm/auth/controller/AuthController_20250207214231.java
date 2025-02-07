package com.doooogh.farm.auth.controller;

import com.doooogh.farm.auth.dto.LoginRequest;
import com.doooogh.farm.auth.dto.TokenResponse;
import com.doooogh.farm.auth.service.AuthService;
import com.doooogh.farm.common.result.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public Result<TokenResponse> login(@RequestBody LoginRequest request) {
        return Result.success(authService.login(request));
    }

    @PostMapping("/refresh")
    public Result<TokenResponse> refresh(@RequestHeader("Authorization") String refreshToken) {
        return Result.success(authService.refresh(refreshToken));
    }

    @PostMapping("/logout")
    public Result<Void> logout(@RequestHeader("Authorization") String accessToken) {
        authService.logout(accessToken);
        return Result.success(null);
    }
} 