package com.doooogh.farm.auth.strategy;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AuthenticationManager {

    private final List<AuthenticationStrategy> strategies;

    public AuthenticationManager(List<AuthenticationStrategy> strategies) {
        this.strategies = strategies;
    }

    public Authentication authenticate(Authentication authentication, String method) throws AuthenticationException {
        for (AuthenticationStrategy strategy : strategies) {
            if (strategy.supports(authentication.getClass())) {
                return strategy.authenticate(authentication);
            }
        }
        throw new AuthenticationException("No suitable authentication strategy found") {};
    }
} 