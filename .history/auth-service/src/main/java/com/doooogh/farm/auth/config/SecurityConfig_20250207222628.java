package com.doooogh.farm.auth.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

/**
 * Spring Security安全配置类
 * 配置认证和授权规则、密码编码器、认证管理器等
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * 配置安全过滤链
     * 定义请求的安全规则和认证方式
     *
     * @param http 安全配置对象
     * @return 配置好的安全过滤链
     * @throws Exception 配置异常
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // 禁用CSRF保护，因为使用JWT
            .csrf(AbstractHttpConfigurer::disable)
            // 配置请求授权规则
            .authorizeHttpRequests(authorize -> authorize
                // 登录和刷新令牌接口允许匿名访问
                .requestMatchers("/auth/login", "/auth/refresh").permitAll()
                // 其他所有请求需要认证
                .anyRequest().authenticated()
            )
            // 配置会话管理，使用无状态会话
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            );
        return http.build();
    }

    /**
     * 配置认证管理器
     * 用于处理认证请求，支持多种认证方式
     *
     * @param config 认证配置
     * @return 认证管理器实例
     * @throws Exception 配置异常
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    /**
     * 配置密码编码器
     * 使用BCrypt加密算法对密码进行加密和验证
     *
     * @return BCrypt密码编码器实例
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
} 