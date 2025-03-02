package com.doooogh.farm.common.exception;

import lombok.Getter;

@Getter
public class AuthException extends RuntimeException {
    private final Integer code;

    public AuthException(Integer code, String message) {
        super(message);
        this.code = code;
    }


    public static AuthException authException(String message) {
        return new AuthException(500,message);
    }


    public static AuthException invalidCredentials() {
        return new AuthException(401001, "用户名或密码错误");
    }

    public static AuthException userNotFound() {
        return new AuthException(401002, "用户不存在");
    }

    public static AuthException userNotLogin() {
        return new AuthException(401002, "用户未登录");
    }

    public static AuthException accountLocked() {
        return new AuthException(401003, "账户已被锁定");
    }

    public static AuthException accountDisabled() {
        return new AuthException(401004, "账户已被禁用");
    }

    public static AuthException accessDenied() {
        return new AuthException(403001, "无权访问该资源");
    }

    public static AuthException tokenExpired() {
        return new AuthException(401005, "token已过期");
    }

    public static AuthException invalidToken() {
        return new AuthException(401006, "无效的token");
    }
    public static AuthException unsupportAuthenticationMethod(String authMethod) {
        return new AuthException(401007, "不支持的认证方式:"+authMethod);
    }
}