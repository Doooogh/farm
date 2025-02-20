package com.doooogh.farm.auth.strategy;

import com.doooogh.farm.common.enums.AuthenticationEnum;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

public interface AuthenticationStrategy {
    Authentication authenticate(Authentication authentication) throws AuthenticationException;

    boolean supports(AuthenticationEnum method);
} 