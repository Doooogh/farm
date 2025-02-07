package com.doooogh.farm.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SmsAuthenticationProvider implements AuthenticationProvider {
    
    private final UserDetailsService userDetailsService;
    private final RedisUtil redisUtil;
    
    @Override
    public Authentication authenticate(Authentication authentication) {
        SmsAuthenticationToken authenticationToken = (SmsAuthenticationToken) authentication;
        String mobile = (String) authenticationToken.getPrincipal();
        String code = (String) authenticationToken.getCredentials();
        
        // 验证码验证
        String cacheCode = redisUtil.get("sms_code:" + mobile);
        if (!code.equals(cacheCode)) {
            throw new BadCredentialsException("验证码错误");
        }
        
        // 加载用户
        UserDetails user = userDetailsService.loadUserByUsername(mobile);
        
        // 返回认证token
        return new SmsAuthenticationToken(user, null, user.getAuthorities());
    }
} 