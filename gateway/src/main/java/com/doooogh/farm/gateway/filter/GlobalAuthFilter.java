package com.doooogh.farm.gateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class GlobalAuthFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        HttpHeaders headers = exchange.getRequest().getHeaders();
        String authToken = headers.getFirst(HttpHeaders.AUTHORIZATION);

        if (authToken == null || !isValidToken(authToken)) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        // 如果需要，可以在这里添加更多的认证逻辑

        return chain.filter(exchange);
    }

    private boolean isValidToken(String token) {
        // 在这里实现令牌验证逻辑
        // 例如，调用认证服务验证JWT令牌
        return true; // 假设令牌有效
    }

    @Override
    public int getOrder() {
        return -1; // 确保过滤器在较早阶段执行
    }
} 