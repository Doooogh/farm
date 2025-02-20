/**
 * Feign配置类
 * 配置Feign客户端的行为
 */
package com.doooogh.farm.user.config;

import com.doooogh.farm.user.feign.FeignErrorDecoder;
import feign.RequestInterceptor;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import javax.servlet.http.HttpServletRequest;
import feign.Retryer;
import java.util.concurrent.TimeUnit;

@Configuration
public class FeignConfig {
    
    /**
     * 配置Feign的错误解码器
     * 将服务返回的错误信息转换为自定义异常
     */
    @Bean
    public ErrorDecoder errorDecoder() {
        return new FeignErrorDecoder();
    }
    
    /**
     * 配置请求拦截器
     * 添加通用的请求头
     */
    @Bean
    public RequestInterceptor requestInterceptor() {
        return template -> {
            // 为服务间调用添加特殊的认证头
            template.header("X-Service-Call", "true");
            template.header("X-Service-Name", "auth-service");
            
            // 获取当前请求的认证信息
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes != null) {
                HttpServletRequest request = attributes.getRequest();
                String token = request.getHeader("Authorization");
                if (token != null) {
                    template.header("Authorization", token);
                }
            }
        };
    }
    
    @Bean
    public Retryer retryer() {
        return new Retryer.Default(100, TimeUnit.SECONDS.toMillis(1), 3);
    }
} 