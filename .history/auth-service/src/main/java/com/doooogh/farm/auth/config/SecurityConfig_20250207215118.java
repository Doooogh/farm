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

/**
 * Spring Security配置类
 * 配置安全相关的功能，包括认证、授权等
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    /**
     * 配置安全过滤链
     * 定义请求的安全规则和认证方式
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf().disable()  // 禁用CSRF保护，因为使用JWT
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)  // 使用无状态会话
            .and()
            .authorizeRequests()
            .antMatchers("/auth/login", "/auth/refresh").permitAll()  // 登录和刷新接口允许匿名访问
            .anyRequest().authenticated();  // 其他请求需要认证
        return http.build();
    }

    /**
     * 配置认证管理器
     * 用于处理认证请求
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    /**
     * 配置密码编码器
     * 用于加密和验证密码
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
} 