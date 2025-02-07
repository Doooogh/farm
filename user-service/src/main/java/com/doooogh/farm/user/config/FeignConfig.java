/**
 * Feign配置类
 * 配置Feign客户端的行为
 */
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
            template.header("X-Service-Name", "user-service");
            // 可以添加其他通用请求头
        };
    }
} 