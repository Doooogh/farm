package com.doooogh.farm.auth.security;

import com.doooogh.farm.auth.strategy.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final AuthenticationManager authenticationManager;

    public CustomAuthenticationProvider(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String method = (String) authentication.getDetails(); // Assume method is passed in details
        return authenticationManager.authenticate(authentication, method);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return true; // Support all types of authentication
    }
} 