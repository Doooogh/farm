package com.doooogh.farm.user.feign;

import com.doooogh.farm.common.exception.AuthException;
import com.doooogh.farm.common.exception.ServiceException;
import com.doooogh.farm.common.result.Result;
import com.doooogh.farm.common.util.JsonUtils;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.security.access.AccessDeniedException;

import java.nio.charset.StandardCharsets;

/**
 * Feign错误解码器
 * 处理服务调用异常
 */
@Slf4j
public class FeignErrorDecoder implements ErrorDecoder {
    
    private final ErrorDecoder defaultErrorDecoder = new Default();
    
    @Override
    public Exception decode(String methodKey, Response response) {
        try {
            // 记录详细的错误信息
            log.error("Feign call failed - Method: {}, Status: {}, Headers: {}", 
                methodKey, response.status(), response.headers());
            
            if (response.status() == 401) {
                return new AuthException(401, "服务调用未授权，请检查认证信息");
            }
            
            if (response.status() >= 500) {
                return new ServiceException("服务暂时不可用");
            }
            
            // 读取错误响应
            String responseBody = IOUtils.toString(response.body().asInputStream(), StandardCharsets.UTF_8.name());
            log.error("Error response body: {}", responseBody);
            
            Result<?> result = JsonUtils.parseObject(responseBody, Result.class);
            if (result != null) {
                return new ServiceException(result.getCode(), result.getMessage());
            }
        } catch (Exception e) {
            log.error("Feign error decode failed", e);
        }
        return defaultErrorDecoder.decode(methodKey, response);
    }
} 