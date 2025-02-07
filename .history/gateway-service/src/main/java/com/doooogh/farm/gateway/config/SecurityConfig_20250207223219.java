package com.doooogh.farm.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

/**
 * 安全配置类
 * 配置认证和授权规则
 */
@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        return http
            .csrf().disable()
            .authorizeExchange()
                .pathMatchers("/auth/login", "/auth/refresh", "/fallback").permitAll()
                .anyExchange().authenticated()
                .and()
            .oauth2ResourceServer()
                .jwt()
                .jwkSetUri("http://auth-service/oauth2/jwks")
                .and()
            .build();
    }
} 