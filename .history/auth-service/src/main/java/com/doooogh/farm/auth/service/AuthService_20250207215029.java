package com.doooogh.farm.auth.service;

import com.doooogh.farm.auth.dto.LoginRequest;
import com.doooogh.farm.auth.dto.TokenResponse;
import com.doooogh.farm.common.exception.AuthException;
import com.doooogh.farm.common.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.TimeUnit;

/**
 * 认证服务
 * 处理用户登录、令牌刷新和登出等认证相关操作
 */
@Service
@RequiredArgsConstructor
public class AuthService {

    /**
     * Spring Security的认证管理器，用于处理用户认证
     */
    private final AuthenticationManager authenticationManager;
    
    /**
     * JWT工具类，用于生成和验证JWT令牌
     */
    private final JwtUtil jwtUtil;
    
    /**
     * Redis工具类，用于管理令牌存储和黑名单
     */
    private final RedisUtil redisUtil;
    
    /**
     * 用户详情服务，用于加载用户信息
     */
    private final UserDetailsService userDetailsService;

    /**
     * 用户登录
     * 验证用户凭据并生成访问令牌和刷新令牌
     *
     * @param request 包含用户名和密码的登录请求
     * @return 包含访问令牌和刷新令牌的响应
     * @throws AuthException 当用户凭据无效时抛出
     */
    public TokenResponse login(LoginRequest request) {
        try {
            // 创建认证令牌并进行认证
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );
            return generateTokens(authentication);
        } catch (Exception e) {
            throw AuthException.invalidCredentials();
        }
    }

    /**
     * 刷新令牌
     * 使用有效的刷新令牌生成新的访问令牌
     *
     * @param refreshToken 刷新令牌
     * @return 包含新的访问令牌和刷新令牌的响应
     * @throws AuthException 当刷新令牌无效或过期时抛出
     */
    public TokenResponse refresh(String refreshToken) {
        String token = extractToken(refreshToken);
        // 验证是否为有效的刷新令牌
        if (!jwtUtil.canTokenBeRefreshed(token)) {
            throw AuthException.invalidToken();
        }
        // 从令牌中获取用户信息并生成新的令牌
        String username = jwtUtil.getUsernameFromToken(token);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        return generateTokens(userDetails);
    }

    /**
     * 用户登出
     * 将访问令牌和刷新令牌加入黑名单
     *
     * @param accessToken 访问令牌
     */
    public void logout(String accessToken) {
        String token = extractToken(accessToken);
        String username = jwtUtil.getUsernameFromToken(token);
        // 删除Redis中存储的令牌信息
        redisUtil.delete("access_token:" + token, "refresh_token:" + token);
    }

    /**
     * 从认证对象生成令牌
     */
    private TokenResponse generateTokens(Authentication authentication) {
        return generateTokens((UserDetails) authentication.getPrincipal());
    }

    /**
     * 生成访问令牌和刷新令牌
     * 
     * @param userDetails 用户详情
     * @return 包含访问令牌和刷新令牌的响应
     */
    private TokenResponse generateTokens(UserDetails userDetails) {
        Instant now = Instant.now();
        // 设置访问令牌有效期为1小时
        Duration accessTokenValidity = Duration.ofHours(1);
        // 设置刷新令牌有效期为7天
        Duration refreshTokenValidity = Duration.ofDays(7);

        // 生成访问令牌和刷新令牌
        String accessToken = jwtUtil.generateToken(userDetails, false);
        String refreshToken = jwtUtil.generateToken(userDetails, true);

        // 将令牌信息存储到Redis，用于维护令牌状态和黑名单
        redisUtil.set(
            "access_token:" + accessToken,
            userDetails.getUsername(),
            accessTokenValidity.toHours(),
            TimeUnit.HOURS
        );
        redisUtil.set(
            "refresh_token:" + refreshToken,
            userDetails.getUsername(),
            refreshTokenValidity.toDays(),
            TimeUnit.DAYS
        );

        // 构建令牌响应
        return TokenResponse.builder()
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .expiresIn(accessTokenValidity.getSeconds())
            .tokenType("Bearer")
            .build();
    }

    /**
     * 从Authorization头中提取令牌
     * 
     * @param authorization Authorization头的值
     * @return 提取的令牌
     * @throws AuthException 当令牌格式无效时抛出
     */
    private String extractToken(String authorization) {
        if (authorization != null && authorization.startsWith("Bearer ")) {
            return authorization.substring(7);
        }
        throw AuthException.invalidToken();
    }
} 