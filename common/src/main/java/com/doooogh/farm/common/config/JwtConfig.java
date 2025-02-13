package com.doooogh.farm.common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * JWT配置类
 * 用于配置JWT令牌的相关参数，包括密钥、过期时间等
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "jwt")
public class JwtConfig {
    /**
     * JWT签名密钥
     * 用于生成和验证JWT令牌的签名
     * 建议在生产环境中使用足够长且安全的密钥
     */
    private String secret;
    
    /**
     * 访问令牌过期时间（小时）
     * 默认为24小时
     */
    private Long tokenExpiration = 24L;
    
    /**
     * 刷新令牌过期时间（天）
     * 默认为7天
     */
    private Long refreshTokenExpiration = 7L;
    
    /**
     * 令牌签发者
     * 用于标识令牌的发行方
     */
    private String issuer = "doooogh.farm";
} 