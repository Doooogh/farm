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

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final RedisUtil redisUtil;
    private final UserDetailsService userDetailsService;

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
        if (!jwtUtil.canTokenBeRefreshed(token)) {
            throw AuthException.invalidToken();
        }
        String username = jwtUtil.getUsernameFromToken(token);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        // 生成新的token
        return generateTokens(userDetails);
    }

    public void logout(String accessToken) {
        String token = extractToken(accessToken);
        String username = jwtUtil.getUsernameFromToken(token);
        redisUtil.delete("access_token:" + token, "refresh_token:" + token);
    }

    private TokenResponse generateTokens(Authentication authentication) {
        return generateTokens((UserDetails) authentication.getPrincipal());
    }

    private TokenResponse generateTokens(UserDetails userDetails) {
        Instant now = Instant.now();
        Duration accessTokenValidity = Duration.ofHours(1);
        Duration refreshTokenValidity = Duration.ofDays(7);

        String accessToken = jwtUtil.generateToken(userDetails, false);
        String refreshToken = jwtUtil.generateToken(userDetails, true);

        // 存储到Redis用于token黑名单
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

        return TokenResponse.builder()
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .expiresIn(accessTokenValidity.getSeconds())
            .tokenType("Bearer")
            .build();
    }

    private String extractToken(String authorization) {
        if (authorization != null && authorization.startsWith("Bearer ")) {
            return authorization.substring(7);
        }
        throw AuthException.invalidToken();
    }
} 