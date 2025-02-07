package com.doooogh.farm.common.util;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONWriter;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * JSON工具类
 * 基于Fastjson2的JSON操作工具类
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JsonUtils {
    
    /**
     * 对象转JSON字符串
     *
     * @param object 对象
     * @return JSON字符串
     */
    public static String toJsonString(Object object) {
        return JSON.toJSONString(object, JSONWriter.Feature.WriteMapNullValue);
    }
    
    /**
     * 对象转JSON字符串，格式化输出
     *
     * @param object 对象
     * @return 格式化的JSON字符串
     */
    public static String toPrettyJsonString(Object object) {
        return JSON.toJSONString(object, 
            JSONWriter.Feature.WriteMapNullValue, 
            JSONWriter.Feature.PrettyFormat);
    }
    
    /**
     * JSON字符串转对象
     *
     * @param text JSON字符串
     * @param clazz 目标类型
     * @return 转换后的对象
     */
    public static <T> T parseObject(String text, Class<T> clazz) {
        return JSON.parseObject(text, clazz);
    }
    
    /**
     * JSON字符串转List
     *
     * @param text JSON字符串
     * @param clazz 目标类型
     * @return 转换后的List
     */
    public static <T> List<T> parseArray(String text, Class<T> clazz) {
        return JSON.parseArray(text, clazz);
    }
    
    /**
     * 对象转JSONObject
     *
     * @param object 对象
     * @return JSONObject
     */
    public static JSONObject toJsonObject(Object object) {
        return JSON.parseObject(toJsonString(object));
    }
    
    /**
     * 对象转换
     * 用于不同类型的对象之间转换
     *
     * @param object 源对象
     * @param clazz 目标类型
     * @return 转换后的对象
     */
    public static <T> T convert(Object object, Class<T> clazz) {
        return JSON.parseObject(toJsonString(object), clazz);
    }
} 