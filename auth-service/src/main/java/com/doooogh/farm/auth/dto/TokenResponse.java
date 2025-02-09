package com.doooogh.farm.auth.dto;

import lombok.Builder;
import lombok.Data;

/**
 * 令牌响应DTO
 * 封装认证成功后返回的令牌信息
 */
@Data
@Builder
public class TokenResponse {
    /**
     * 访问令牌
     * 用于访问受保护资源的令牌
     */
    private String accessToken;
    
    /**
     * 刷新令牌
     * 用于获取新的访问令牌的令牌
     */
    private String refreshToken;
    
    /**
     * 访问令牌有效期
     * 单位：秒
     */
    private Long expiresIn;
    
    /**
     * 令牌类型
     * 固定值："Bearer"
     */
    private String tokenType;
    
    private String status;
} 