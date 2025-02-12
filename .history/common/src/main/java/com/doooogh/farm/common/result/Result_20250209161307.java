package com.doooogh.farm.common.result;

/**
 * 统一返回结果类
 * @param <T> 数据类型
 */
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

    // Getter and Setter methods
    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

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
} 