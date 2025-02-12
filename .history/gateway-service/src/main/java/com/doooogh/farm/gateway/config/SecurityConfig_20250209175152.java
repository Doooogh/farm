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
        http
            .authorizeExchange()
                .pathMatchers("/auth/**", "/actuator/**", "/fallback").permitAll()
                .anyExchange().authenticated()
            .and()
            .csrf().disable()
            .oauth2ResourceServer()
                .jwt();
                
        return http.build();
    }
} 