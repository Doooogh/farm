package com.doooogh.farm.auth.controller;

import com.doooogh.farm.auth.dto.LoginRequest;
import com.doooogh.farm.auth.dto.TokenResponse;
import com.doooogh.farm.auth.service.AuthService;
import com.doooogh.farm.common.result.Result;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 认证控制器
 * 处理用户认证相关的HTTP请求，包括登录、刷新令牌和登出
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;

    /**
     * 用户登录接口
     * 验证用户凭据并返回访问令牌和刷新令牌
     *
     * @param request 包含用户名和密码的登录请求
     * @return 包含访问令牌和刷新令牌的响应
     */
    @PostMapping("/login")
    public Result<TokenResponse> login(HttpServletRequest request, @RequestBody LoginRequest loginRequest) {
            TokenResponse response = authService.login(request,loginRequest);
            return Result.ok(response);
    }

    /**
     * 刷新令牌接口
     * 使用有效的刷新令牌获取新的访问令牌
     *
     * @param refreshToken 刷新令牌，从请求头的Authorization字段获取
     * @return 包含新的访问令牌和刷新令牌的响应
     */
    @PostMapping("/refresh")
    public Result<TokenResponse> refresh(@RequestHeader("Authorization") String refreshToken) {
        try {
            TokenResponse tokenResponse = authService.refresh(refreshToken);
            return Result.ok(tokenResponse);
        } catch (Exception e) {
            return Result.fail("刷新令牌失败");
        }
    }

    /**
     * 用户登出接口
     * 使当前的访问令牌失效
     *
     * @param accessToken 访问令牌，从请求头的Authorization字段获取
     * @return 登出成功的响应
     */
    @PostMapping("/logout")
    public Result<Void> logout(@RequestHeader("Authorization") String accessToken) {
        authService.logout(accessToken);
        return Result.ok();
    }

    @GetMapping("/status")
    public String status() {
        return "Auth service is running";
    }
} 