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

    // Static factory methods
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

    /**
     * 创建错误响应
     * 为了保持向后兼容，建议使用 fail 方法替代
     *
     * @param code 错误码
     * @param message 错误信息
     * @return 错误响应结果
     * @deprecated 请使用 {@link #fail(Integer, String)} 替代
     */
    @Deprecated
    public static <T> Result<T> error(Integer code, String message) {
        return fail(code, message);
    }

    /**
     * 创建错误响应
     * 为了保持向后兼容，建议使用 fail 方法替代
     *
     * @param message 错误信息
     * @return 错误响应结果
     * @deprecated 请使用 {@link #fail(String)} 替代
     */
    @Deprecated
    public static <T> Result<T> error(String message) {
        return fail(message);
    }

    /**
     * 创建错误响应
     * 为了保持向后兼容，建议使用 fail 方法替代
     *
     * @return 错误响应结果
     * @deprecated 请使用 {@link #fail()} 替代
     */
    @Deprecated
    public static <T> Result<T> error() {
        return fail();
    }

    /**
     * 创建成功响应
     * 为了保持向后兼容，建议使用 ok 方法替代
     *
     * @param data 响应数据
     * @return 成功响应结果
     * @deprecated 请使用 {@link #ok(Object)} 替代
     */
    @Deprecated
    public static <T> Result<T> success(T data) {
        return ok(data);
    }

    /**
     * 创建成功响应
     * 为了保持向后兼容，建议使用 ok 方法替代
     *
     * @return 成功响应结果
     * @deprecated 请使用 {@link #ok()} 替代
     */
    @Deprecated
    public static <T> Result<T> success() {
        return ok();
    }

    /**
     * 创建成功响应
     * 为了保持向后兼容，建议使用 ok 方法替代
     *
     * @param data 响应数据
     * @param message 响应消息
     * @return 成功响应结果
     * @deprecated 请使用 {@link #ok(Object, String)} 替代
     */
    @Deprecated
    public static <T> Result<T> success(T data, String message) {
        return ok(data, message);
    }
} 