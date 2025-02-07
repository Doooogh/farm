package com.doooogh.farm.gateway.config;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.cloud.gateway.filter.ratelimit.RedisRateLimiter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

/**
 * 网关配置类
 * 配置路由、过滤器等
 */
@Configuration
public class GatewayConfig {

    /**
     * 路由定位器
     * 配置路由规则和过滤器
     */
    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
            // 认证服务路由
            .route("auth-service", r -> r.path("/auth/**")
                .filters(f -> f
                    .requestRateLimiter(c -> c
                        .setRateLimiter(redisRateLimiter())
                        .setKeyResolver(ipKeyResolver()))
                    .circuitBreaker(c -> c
                        .setName("authFallback")
                        .setFallbackUri("forward:/fallback")))
                .uri("lb://auth-service"))
            // 用户服务路由
            .route("user-service", r -> r.path("/users/**")
                .filters(f -> f
                    .requestRateLimiter(c -> c
                        .setRateLimiter(redisRateLimiter())
                        .setKeyResolver(ipKeyResolver()))
                    .circuitBreaker(c -> c
                        .setName("userFallback")
                        .setFallbackUri("forward:/fallback")))
                .uri("lb://user-service"))
            .build();
    }

    /**
     * Redis限流器
     * 配置限流规则
     */
    @Bean
    public RedisRateLimiter redisRateLimiter() {
        // 令牌桶算法：每秒产生10个令牌，桶容量为20
        return new RedisRateLimiter(10, 20);
    }

    /**
     * IP地址限流键解析器
     * 基于IP地址进行限流
     */
    @Bean
    public KeyResolver ipKeyResolver() {
        return exchange -> Mono.just(
            exchange.getRequest().getRemoteAddress().getAddress().getHostAddress());
    }
} 