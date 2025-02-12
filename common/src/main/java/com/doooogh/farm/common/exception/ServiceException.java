package com.doooogh.farm.common.exception;

import lombok.Getter;

/**
 * 服务层异常
 * 用于处理业务逻辑异常
 */
@Getter
public class ServiceException extends RuntimeException {
    
    private final Integer code;
    
    public ServiceException(String message) {
        super(message);
        this.code = 500;
    }
    
    public ServiceException(Integer code, String message) {
        super(message);
        this.code = code;
    }
} 