package com.doooogh.farm.auth.security;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final ConcurrentHashMap<String, AtomicInteger> loginAttempts = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, String> captchaStore = new ConcurrentHashMap<>();

    public CustomAuthenticationProvider(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = (String) authentication.getCredentials();
        String captcha = (String) authentication.getDetails(); // 假设验证码通过details传递

        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        if (userDetails == null || !passwordEncoder.matches(password, userDetails.getPassword())) {
            AtomicInteger attempts = loginAttempts.computeIfAbsent(username, k -> new AtomicInteger(0));
            if (attempts.incrementAndGet() >= 5) {
                if (!validateCaptcha(username, captcha)) {
                    throw new BadCredentialsException("验证码错误");
                }
            } else if (attempts.get() == 5) {
                String generatedCaptcha = generateCaptcha(username);
                System.out.println("生成的验证码: " + generatedCaptcha); // 仅用于调试
                throw new BadCredentialsException("需要验证码");
            }
            throw new BadCredentialsException("用户名或密码错误");
        }

        // 重置尝试计数和清除验证码
        loginAttempts.remove(username);
        clearCaptcha(username);

        return new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }

    private String generateCaptcha(String username) {
        String captcha = String.valueOf(new Random().nextInt(9999));
        captchaStore.put(username, captcha);
        return captcha;
    }

    private boolean validateCaptcha(String username, String captcha) {
        String storedCaptcha = captchaStore.get(username);
        return storedCaptcha != null && storedCaptcha.equals(captcha);
    }

    private void clearCaptcha(String username) {
        captchaStore.remove(username);
    }
} 