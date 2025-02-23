package com.doooogh.farm.auth.security;

import com.alibaba.fastjson2.JSONObject;
import com.doooogh.farm.auth.entity.CustomAuthenticationToken;
import com.doooogh.farm.common.enums.AuthenticationEnum;
import com.doooogh.farm.auth.strategy.CusAuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final CusAuthenticationManager cusAuthenticationManager;

    public CustomAuthenticationProvider(CusAuthenticationManager cusAuthenticationManager) {
        this.cusAuthenticationManager = cusAuthenticationManager;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        CustomAuthenticationToken customAuthenticationToken= (CustomAuthenticationToken) authentication;
        AuthenticationEnum method = determineAuthenticationMethod(customAuthenticationToken);
        return cusAuthenticationManager.authenticate(authentication, method);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return true; // Support all types of authentication
    }


    // 根据参数或其他信息判断认证方式
    private AuthenticationEnum determineAuthenticationMethod(CustomAuthenticationToken customAuthenticationToken) {
        // 假设使用请求头来判断认证方式
        String authMethod = customAuthenticationToken.getAuthMethod();
        AuthenticationEnum authenticationEnum = AuthenticationEnum.getAuthenticationType(authMethod);
        return authenticationEnum;
    }
} 