package com.doooogh.farm.common.result;

import lombok.Data;

/**
 * 统一返回结果类
 * @param <T> 数据类型
 */
@Data
public class Result<T> {
    
    /**
     * 状态码
     */
    private Integer code;
    
    /**
     * 返回信息
     */
    private String message;
    
    /**
     * 返回数据
     */
    private T data;
    
    /**
     * 是否成功
     */
    private Boolean success;

    /**
     * 私有构造函数
     */
    private Result() {}

    /**
     * 成功返回结果
     */
    public static <T> Result<T> ok() {
        Result<T> result = new Result<>();
        result.setCode(200);
        result.setSuccess(true);
        result.setMessage("操作成功");
        return result;
    }

    /**
     * 成功返回结果
     * @param data 返回的数据
     */
    public static <T> Result<T> ok(T data) {
        Result<T> result = ok();
        result.setData(data);
        return result;
    }

    /**
     * 成功返回结果
     * @param data 返回的数据
     * @param message 返回的消息
     */
    public static <T> Result<T> ok(T data, String message) {
        Result<T> result = ok();
        result.setData(data);
        result.setMessage(message);
        return result;
    }

    /**
     * 失败返回结果
     */
    public static <T> Result<T> fail() {
        Result<T> result = new Result<>();
        result.setCode(500);
        result.setSuccess(false);
        result.setMessage("操作失败");
        return result;
    }

    /**
     * 失败返回结果
     * @param message 错误信息
     */
    public static <T> Result<T> fail(String message) {
        Result<T> result = fail();
        result.setMessage(message);
        return result;
    }

    /**
     * 失败返回结果
     * @param code 错误码
     * @param message 错误信息
     */
    public static <T> Result<T> fail(Integer code, String message) {
        Result<T> result = fail();
        result.setCode(code);
        result.setMessage(message);
        return result;
    }

    /**
     * 参数验证失败返回结果
     */
    public static <T> Result<T> validateFailed() {
        return fail(400, "参数检验失败");
    }

    /**
     * 参数验证失败返回结果
     * @param message 提示信息
     */
    public static <T> Result<T> validateFailed(String message) {
        return fail(400, message);
    }

    /**
     * 未登录返回结果
     */
    public static <T> Result<T> unauthorized() {
        return fail(401, "暂未登录或token已经过期");
    }

    /**
     * 未授权返回结果
     */
    public static <T> Result<T> forbidden() {
        return fail(403, "没有相关权限");
    }
} 