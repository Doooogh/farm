package com.doooogh.farm.gateway.controller;

import com.doooogh.farm.common.result.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * 降级处理控制器
 * 处理服务不可用时的降级响应
 */
@RestController
public class FallbackController {

    @GetMapping("/fallback")
    public Result<Void> fallback() {
        return Result.fail(503, "服务不可用，请稍后重试");
    }
} 