package com.doooogh.farm.auth.strategy;

import com.doooogh.farm.auth.enums.AuthenticationEnum;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CusAuthenticationManager {

    private final List<AuthenticationStrategy> strategies;

    public CusAuthenticationManager(@Lazy List<AuthenticationStrategy> strategies) {
        this.strategies = strategies;
    }

    public Authentication authenticate(Authentication authentication, AuthenticationEnum method) throws AuthenticationException {
        for (AuthenticationStrategy strategy : strategies) {
            if (strategy.supports(method)) {
                return strategy.authenticate(authentication);
            }
        }
        throw new AuthenticationException("No suitable authentication strategy found") {};
    }
} 