package com.doooogh.farm.auth.strategy;

import com.doooogh.farm.auth.service.SmsService;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

@Component
public class SmsAuthenticationStrategy implements AuthenticationStrategy {

    private final UserDetailsService userDetailsService;
    private final SmsService smsService;

    public SmsAuthenticationStrategy(UserDetailsService userDetailsService, SmsService smsService) {
        this.userDetailsService = userDetailsService;
        this.smsService = smsService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String phoneNumber = authentication.getName();
        String smsCode = (String) authentication.getCredentials();

        if (!smsService.validateSmsCode(phoneNumber, smsCode)) {
            throw new BadCredentialsException("Invalid SMS code");
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(phoneNumber);

        return new UsernamePasswordAuthenticationToken(userDetails, smsCode, userDetails.getAuthorities());
    }
} 