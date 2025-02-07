package com.doooogh.farm.common.result;

import lombok.Data;

/**
 * 统一响应结果类
 * 封装所有接口的响应数据格式
 *
 * @param <T> 响应数据的类型
 */
@Data
public class Result<T> {
    /**
     * 响应状态码
     * 200表示成功，其他值表示失败
     */
    private Integer code;
    
    /**
     * 响应消息
     * 成功时为"success"，失败时为具体的错误信息
     */
    private String message;
    
    /**
     * 响应数据
     * 成功时返回的具体数据
     */
    private T data;

    /**
     * 创建成功响应
     *
     * @param data 响应数据
     * @return 成功响应结果
     */
    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<>();
        result.setCode(200);
        result.setMessage("success");
        result.setData(data);
        return result;
    }

    /**
     * 创建错误响应
     *
     * @param code 错误码
     * @param message 错误信息
     * @return 错误响应结果
     */
    public static <T> Result<T> error(Integer code, String message) {
        Result<T> result = new Result<>();
        result.setCode(code);
        result.setMessage(message);
        return result;
    }
} 