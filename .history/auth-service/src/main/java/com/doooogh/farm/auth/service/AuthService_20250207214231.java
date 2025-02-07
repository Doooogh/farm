package com.doooogh.farm.auth.service;

import com.doooogh.farm.auth.dto.LoginRequest;
import com.doooogh.farm.auth.dto.TokenResponse;
import com.doooogh.farm.common.exception.AuthException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtEncoder jwtEncoder;
    private final RedisTemplate<String, String> redisTemplate;

    public TokenResponse login(LoginRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );
            return generateTokens(authentication);
        } catch (Exception e) {
            throw AuthException.invalidCredentials();
        }
    }

    public TokenResponse refresh(String refreshToken) {
        String token = extractToken(refreshToken);
        String username = redisTemplate.opsForValue().get("refresh_token:" + token);
        if (username == null) {
            throw AuthException.invalidToken();
        }
        // 生成新的token
        return generateTokens(username);
    }

    public void logout(String accessToken) {
        String token = extractToken(accessToken);
        redisTemplate.delete("access_token:" + token);
        redisTemplate.delete("refresh_token:" + token);
    }

    private TokenResponse generateTokens(Authentication authentication) {
        return generateTokens(authentication.getName());
    }

    private TokenResponse generateTokens(String username) {
        Instant now = Instant.now();
        Duration accessTokenValidity = Duration.ofHours(1);
        Duration refreshTokenValidity = Duration.ofDays(7);

        String accessToken = generateToken(username, now, accessTokenValidity);
        String refreshToken = generateToken(username, now, refreshTokenValidity);

        // 存储到Redis
        redisTemplate.opsForValue().set(
            "access_token:" + accessToken,
            username,
            accessTokenValidity
        );
        redisTemplate.opsForValue().set(
            "refresh_token:" + refreshToken,
            username,
            refreshTokenValidity
        );

        return TokenResponse.builder()
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .expiresIn(accessTokenValidity.getSeconds())
            .tokenType("Bearer")
            .build();
    }

    private String generateToken(String username, Instant now, Duration validity) {
        // 实现JWT token生成逻辑
        return "token"; // 这里需要实现具体的JWT生成逻辑
    }

    private String extractToken(String authorization) {
        if (authorization != null && authorization.startsWith("Bearer ")) {
            return authorization.substring(7);
        }
        throw AuthException.invalidToken();
    }
} 