package com.doooogh.farm.auth.strategy;

import com.alibaba.fastjson2.JSON;
import com.doooogh.farm.common.enums.AuthenticationEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class CusAuthenticationManager {

    private final List<AuthenticationStrategy> strategies;

    public CusAuthenticationManager(@Lazy List<AuthenticationStrategy> strategies) {
        this.strategies = strategies;
    }

    public Authentication authenticate(Authentication authentication, AuthenticationEnum method) throws AuthenticationException {
        for (AuthenticationStrategy strategy : strategies) {
            if (strategy.supports(method)) {
                Authentication authenticate = strategy.authenticate(authentication);
                log.info("Authenticate result:{}", JSON.toJSONString(authenticate));
                if(authenticate.isAuthenticated()){
                    SecurityContext context = SecurityContextHolder.createEmptyContext();
                    context.setAuthentication(authenticate);
                    SecurityContextHolder.setContext(context);
                }
                return authenticate;
            }
        }
        throw new AuthenticationException("No suitable authentication strategy found") {};
    }
} 