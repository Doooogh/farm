package com.doooogh.farm.user.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
            // 允许认证服务调用用户查询接口
            .antMatchers("/api/users/find").permitAll()
            .anyRequest().authenticated()
            .and()
            .csrf().disable();
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        // 使用你的JWK Set URI
        String jwkSetUri = "http://auth-service/oauth2/jwks";
        return NimbusJwtDecoder.withJwkSetUri(jwkSetUri).build();
    }
} 