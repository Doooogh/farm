package com.doooogh.farm.auth.exception;

import lombok.Getter;

@Getter
public class AuthException extends RuntimeException {
    
    private final Integer code;
    
    public AuthException(String message) {
        super(message);
        this.code = 401;
    }
    
    public AuthException(Integer code, String message) {
        super(message);
        this.code = code;
    }
} 