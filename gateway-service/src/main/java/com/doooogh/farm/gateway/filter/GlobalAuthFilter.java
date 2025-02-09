package com.doooogh.farm.gateway.filter;

import com.doooogh.farm.common.result.Result;
import com.doooogh.farm.common.util.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 全局认证过滤器
 * 处理请求的认证信息
 */
@Slf4j
@Component
public class GlobalAuthFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();
        
        // 记录请求日志
        log.info("Request path: {}, method: {}", path, request.getMethod());
        
        // 可以在这里添加其他全局处理逻辑
        // 例如：验证token、添加请求头等
        
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return -1; // 在其他过滤器之前执行
    }

    private Mono<Void> unauthorized(ServerHttpResponse response) {
        Result<Void> result = Result.fail(401, "未授权访问");
        return response(response, result);
    }

    private Mono<Void> forbidden(ServerHttpResponse response) {
        Result<Void> result = Result.fail(403, "没有权限访问");
        return response(response, result);
    }

    private Mono<Void> response(ServerHttpResponse response, Result<?> result) {
        response.setStatusCode(HttpStatus.OK);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        String body = JsonUtils.toJsonString(result);
        DataBuffer buffer = response.bufferFactory().wrap(body.getBytes());
        return response.writeWith(Mono.just(buffer));
    }
} 