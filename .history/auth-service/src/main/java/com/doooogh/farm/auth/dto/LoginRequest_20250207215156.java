package com.doooogh.farm.auth.dto;

import lombok.Data;

/**
 * 登录请求DTO
 * 封装用户登录时提交的认证信息
 */
@Data
public class LoginRequest {
    /**
     * 用户名
     * 用户的登录账号
     */
    private String username;
    
    /**
     * 密码
     * 用户的登录密码
     */
    private String password;
} 