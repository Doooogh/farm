package com.doooogh.farm.auth.service;

import com.doooogh.farm.auth.dto.LoginRequest;
import com.doooogh.farm.auth.dto.TokenResponse;
import com.doooogh.farm.common.exception.AuthException;
import com.doooogh.farm.common.exception.ServiceException;
import com.doooogh.farm.common.result.Result;
import com.doooogh.farm.common.util.JwtUtil;
import com.doooogh.farm.common.util.RedisUtil;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * 认证服务
 * 处理用户登录、令牌刷新和登出等认证相关操作
 */
@Service
@RequiredArgsConstructor
@Slf4j
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
            // 1. 进行用户名密码认证
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    request.getUsername(),
                    request.getPassword()
                )
            );
            
            // 2. 认证成功，生成令牌
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String accessToken = jwtUtil.generateToken(userDetails, false);
            String refreshToken = jwtUtil.generateToken(userDetails, true);
            
            // 3. 将刷新令牌存入Redis
            redisUtil.set("refresh_token:" + userDetails.getUsername(), 
                refreshToken, 
                jwtUtil.getRefreshTokenExpiration(), 
                TimeUnit.DAYS);
            
            // 4. 返回令牌信息
            return TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .expiresIn(jwtUtil.getAccessTokenExpiration() * 3600)
                .tokenType("Bearer")
                .build();
                
        } catch (BadCredentialsException e) {
            throw new AuthException(401, "用户名或密码错误");
        } catch (DisabledException e) {
            throw new AuthException(401, "账户已被禁用");
        } catch (LockedException e) {
            throw new AuthException(401, "账户已被锁定");
        } catch (Exception e) {
            throw new ServiceException("登录服务异常");
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
        try {
            // 1. 验证刷新令牌
            if (!jwtUtil.validateToken(refreshToken)) {
                throw new AuthException(401, "无效的刷新令牌");
            }
            
            // 2. 从令牌中获取用户名
            String username = jwtUtil.getUsernameFromToken(refreshToken);
            
            // 3. 检查Redis中的刷新令牌
            String storedToken = (String) redisUtil.get("refresh_token:" + username);
            if (storedToken == null || !storedToken.equals(refreshToken)) {
                throw new AuthException(401, "刷新令牌已失效");
            }
            
            // 4. 生成新的令牌
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            String newAccessToken = jwtUtil.generateToken(userDetails, false);
            String newRefreshToken = jwtUtil.generateToken(userDetails, true);
            
            // 5. 更新Redis中的刷新令牌
            redisUtil.set("refresh_token:" + username, 
                newRefreshToken, 
                jwtUtil.getRefreshTokenExpiration(), 
                TimeUnit.DAYS);
            
            return TokenResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .expiresIn(jwtUtil.getAccessTokenExpiration() * 3600)
                .tokenType("Bearer")
                .build();
                
        } catch (JwtException e) {
            throw new AuthException(401, "刷新令牌解析失败");
        } catch (Exception e) {
            throw new ServiceException("刷新令牌服务异常");
        }
    }

    /**
     * 用户登出
     * 将访问令牌和刷新令牌加入黑名单
     *
     * @param accessToken 访问令牌
     */
    public void logout(String accessToken) {
        try {
            String username = jwtUtil.getUsernameFromToken(accessToken);
            // 从Redis中删除刷新令牌
            redisUtil.delete("refresh_token:" + username);
            // 将访问令牌加入黑名单
            redisUtil.set("token_blacklist:" + accessToken, "", 
                jwtUtil.getAccessTokenExpiration(), 
                TimeUnit.HOURS);
        } catch (JwtException e) {
            log.warn("Invalid token during logout", e);
        }
    }
} 