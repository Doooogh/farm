package com.doooogh.farm.common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "jwt")
public class JwtConfig {
    /**
     * 密钥
     */
    private String secret = "your-secret-key-your-secret-key-your-secret-key";
    
    /**
     * token过期时间（小时）
     */
    private Long tokenExpiration = 24L;
    
    /**
     * refresh token过期时间（天）
     */
    private Long refreshTokenExpiration = 7L;
    
    /**
     * token签发者
     */
    private String issuer = "com.doooogh.farm";
} 