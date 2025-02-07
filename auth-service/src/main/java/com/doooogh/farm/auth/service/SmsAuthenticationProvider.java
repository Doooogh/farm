package com.doooogh.farm.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

/**
 * 短信验证码认证提供者
 * 实现AuthenticationProvider接口，提供短信验证码认证逻辑
 */
@Component
@RequiredArgsConstructor
public class SmsAuthenticationProvider implements AuthenticationProvider {
    
    private final UserDetailsService userDetailsService;
    private final RedisUtil redisUtil;
    
    /**
     * 执行认证逻辑
     * 验证短信验证码，并加载用户信息
     *
     * @param authentication 包含手机号和验证码的认证信息
     * @return 认证成功的Authentication对象
     * @throws BadCredentialsException 当验证码错误时抛出
     */
    @Override
    public Authentication authenticate(Authentication authentication) {
        SmsAuthenticationToken authenticationToken = (SmsAuthenticationToken) authentication;
        String mobile = (String) authenticationToken.getPrincipal();
        String code = (String) authenticationToken.getCredentials();
        
        // 从Redis中获取验证码并验证
        String cacheCode = redisUtil.get("sms_code:" + mobile);
        if (!code.equals(cacheCode)) {
            throw new BadCredentialsException("验证码错误");
        }
        
        // 验证通过后加载用户信息
        UserDetails user = userDetailsService.loadUserByUsername(mobile);
        
        // 返回已认证的token
        return new SmsAuthenticationToken(user, null, user.getAuthorities());
    }
    
    /**
     * 判断是否支持此认证方式
     *
     * @param authentication 认证信息
     * @return 是否支持
     */
    @Override
    public boolean supports(Class<?> authentication) {
        return SmsAuthenticationToken.class.isAssignableFrom(authentication);
    }
} 