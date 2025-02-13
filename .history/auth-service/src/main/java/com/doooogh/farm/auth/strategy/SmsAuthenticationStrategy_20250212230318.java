package com.doooogh.farm.auth.strategy;

import com.doooogh.farm.auth.service.SmsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

@Component
@Slf4j
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

        log.info("Attempting SMS authentication for phone number {}", phoneNumber);

        // 验证短信验证码
        if (!smsService.validateSmsCode(phoneNumber, smsCode)) {
            log.warn("Invalid SMS code for phone number {}", phoneNumber);
            throw new BadCredentialsException("Invalid SMS code");
        }

        // 加载用户详细信息
        UserDetails userDetails = userDetailsService.loadUserByUsername(phoneNumber);
        if (userDetails == null) {
            log.warn("No user found for phone number {}", phoneNumber);
            throw new BadCredentialsException("User not found");
        }

        log.info("SMS authentication successful for phone number {}", phoneNumber);
        return new UsernamePasswordAuthenticationToken(userDetails, smsCode, userDetails.getAuthorities());
    }

    @Override
    public boolean supports(String method) {
        return "SMS".equalsIgnoreCase(method);
    }

} 