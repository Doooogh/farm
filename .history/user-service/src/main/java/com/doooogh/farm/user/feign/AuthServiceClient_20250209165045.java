package com.doooogh.farm.user.feign;

import com.doooogh.farm.common.result.Result;
import com.doooogh.farm.user.model.TokenInfo;
import com.doooogh.farm.user.model.UserInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 * 认证服务Feign客户端
 * 用于调用认证服务的接口
 */
@FeignClient(name = "auth-service")
public interface AuthServiceClient {
    
    /**
     * 验证令牌
     *
     * @param token 访问令牌
     * @return 令牌信息
     */
    @GetMapping("/auth/token/validate")
    Result<TokenInfo> validateToken(@RequestHeader("Authorization") String token);
    
    /**
     * 获取用户信息
     *
     * @param userId 用户ID
     * @return 用户信息
     */
    @GetMapping("/auth/users/{userId}")
    Result<UserInfo> getUserInfo(@PathVariable("userId") Long userId);
} 