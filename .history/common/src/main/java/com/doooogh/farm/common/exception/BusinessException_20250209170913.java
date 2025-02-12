package com.doooogh.farm.common.exception;

import lombok.Getter;

/**
 * 业务异常
 * 用于处理可预见的业务逻辑异常
 */
@Getter
public class BusinessException extends RuntimeException {
    
    private final Integer code;
    
    public BusinessException(String message) {
        super(message);
        this.code = 400;
    }
    
    public BusinessException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    /**
     * 参数错误
     */
    public static BusinessException invalidParameter(String message) {
        return new BusinessException(400, message);
    }

    /**
     * 数据不存在
     */
    public static BusinessException notFound(String message) {
        return new BusinessException(404, message);
    }

    /**
     * 数据已存在
     */
    public static BusinessException alreadyExists(String message) {
        return new BusinessException(409, message);
    }

    /**
     * 操作被拒绝
     */
    public static BusinessException operationDenied(String message) {
        return new BusinessException(403, message);
    }
} 