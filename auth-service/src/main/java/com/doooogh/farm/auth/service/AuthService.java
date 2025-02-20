package com.doooogh.farm.auth.service;

import com.alibaba.fastjson2.JSONObject;
import com.doooogh.farm.auth.dto.LoginRequest;
import com.doooogh.farm.auth.dto.TokenResponse;
import com.doooogh.farm.common.auth.CustomUserDetails;
import com.doooogh.farm.common.enums.AuthenticationEnum;
import com.doooogh.farm.auth.security.CustomAuthenticationProvider;
import com.doooogh.farm.common.exception.AuthException;
import com.doooogh.farm.common.exception.ServiceException;
import com.doooogh.farm.common.util.JwtUtil;
import com.doooogh.farm.common.util.RedisUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
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

    private final CustomAuthenticationProvider customAuthenticationProvider;

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

    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 用户登录
     * 验证用户凭据并生成访问令牌和刷新令牌
     *
     * @param request 包含用户名和密码的登录请求
     * @return 包含访问令牌和刷新令牌的响应
     * @throws AuthException 当用户凭据无效时抛出
     */
    public TokenResponse login(HttpServletRequest request, LoginRequest loginRequest) {
        try {
            // 根据请求决定认证方式
            AuthenticationEnum authenticationEnum = determineAuthenticationMethod(request);  // 根据请求判断认证方式
            // 从请求体中读取 JSON 数据

            String username = loginRequest.getUsername();
            String password = loginRequest.getPassword();
            String captcha = loginRequest.getCaptcha();
            String phone = loginRequest.getPhone();
            String smsCode = loginRequest.getSmsCode();
            AbstractAuthenticationToken authRequest = null;
            switch (authenticationEnum) {
                case USERNAME_PASSWORD:
                    authRequest = new UsernamePasswordAuthenticationToken(username, password);
                    break;
                case SMS:
                    authRequest = new UsernamePasswordAuthenticationToken(phone, smsCode);
                    break;
                default:
                    throw new AuthenticationException("Unsupported authentication method: " + authenticationEnum) {
                    };
            }
            JSONObject details = new JSONObject();
            details.put("authMethod", authenticationEnum);
            details.put("captcha", captcha);
            //todo 其他的信息
            authRequest.setDetails(details);
            Authentication authentication = customAuthenticationProvider.authenticate(authRequest);// 将请求交给 AuthenticationManager 进行认证

            // 2. 认证成功，生成令牌
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            userDetails.setAuthenticationEnum(authenticationEnum);
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

        } catch (AuthException e) {
            throw e;
        } catch (BadCredentialsException e) {
            throw AuthException.invalidCredentials();
        } catch (DisabledException e) {
            throw AuthException.accountDisabled();
        } catch (LockedException e) {
            throw AuthException.accountLocked();
        } catch (Exception e) {
            throw AuthException.authException("登录服务异常");
        }
    }


    // 根据请求头、参数或其他信息判断认证方式
    private AuthenticationEnum determineAuthenticationMethod(HttpServletRequest request) {
        // 假设使用请求头来判断认证方式
        String authMethod = request.getHeader("Auth-Method");
        AuthenticationEnum authenticationEnum = AuthenticationEnum.getAuthenticationType(authMethod);
        return authenticationEnum;
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
                throw AuthException.invalidToken();
            }

            // 2. 从令牌中获取用户名
            String username = jwtUtil.getUsernameFromToken(refreshToken);

            // 3. 检查Redis中的刷新令牌
            String storedToken = (String) redisUtil.get("refresh_token:" + username);
            if (storedToken == null || !storedToken.equals(refreshToken)) {
                throw AuthException.tokenExpired();
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
        } catch (AuthException e) {
            throw e;
        } catch (Exception e) {
            throw new AuthException(500, "刷新令牌服务异常");
        }
    }

    /**
     * 用户登出
     * 将访问令牌和刷新令牌加入黑名单
     *
     * @param accessToken 访问令牌
     */
    public void logout(String accessToken) {
        String username = jwtUtil.getUsernameFromToken(accessToken);
        if(null==username){
            throw AuthException.userNotLogin();
        }
        // 从Redis中删除刷新令牌
        redisUtil.delete("refresh_token:" + username);
        // 将访问令牌加入黑名单
        redisUtil.set("token_blacklist:" + accessToken, "",
                jwtUtil.getAccessTokenExpiration(),
                TimeUnit.HOURS);

    }
} 