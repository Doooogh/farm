package com.doooogh.farm.auth.security;

import com.alibaba.fastjson2.JSONObject;
import com.doooogh.farm.auth.enums.AuthenticationEnum;
import com.doooogh.farm.auth.strategy.CusAuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final CusAuthenticationManager cusAuthenticationManager;

    public CustomAuthenticationProvider(CusAuthenticationManager cusAuthenticationManager) {
        this.cusAuthenticationManager = cusAuthenticationManager;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        JSONObject details = (JSONObject) authentication.getDetails();
        AuthenticationEnum method = (AuthenticationEnum) details.get("authMethod");
        return cusAuthenticationManager.authenticate(authentication, method);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return true; // Support all types of authentication
    }
} 