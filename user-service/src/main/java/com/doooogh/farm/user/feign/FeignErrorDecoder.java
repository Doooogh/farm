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
            // 读取错误响应
            String body = IOUtils.toString(response.body().asInputStream(), StandardCharsets.UTF_8);
            Result<?> result = JsonUtils.parseObject(body, Result.class);
            
            // 根据错误码返回对应的异常
            if (result != null) {
                switch (result.getCode()) {
                    case 401001:
                        return new AuthException(result.getCode(), result.getMessage());
                    case 403001:
                        return new AccessDeniedException(result.getMessage());
                    default:
                        return new ServiceException(result.getCode(), result.getMessage());
                }
            }
        } catch (Exception e) {
            log.error("Feign error decode failed", e);
        }
        return defaultErrorDecoder.decode(methodKey, response);
    }
} 