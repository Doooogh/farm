package com.doooogh.farm.auth.strategy;

import com.doooogh.farm.auth.service.CaptchaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@Slf4j
public class UsernamePasswordAuthenticationStrategy implements AuthenticationStrategy {

    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    private final CaptchaService captchaService;
    private final ConcurrentHashMap<String, AtomicInteger> loginAttempts = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, String> captchaStore = new ConcurrentHashMap<>();



    public UsernamePasswordAuthenticationStrategy(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder, CaptchaService captchaService) {
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
        this.captchaService = captchaService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = (String) authentication.getCredentials();
        String captcha = (String) authentication.getDetails(); // 假设验证码通过details传递

        log.info("Attempting authentication for user {}", username);

        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        if (userDetails == null || !passwordEncoder.matches(password, userDetails.getPassword())) {
            AtomicInteger attempts = loginAttempts.computeIfAbsent(username, k -> new AtomicInteger(0));
            if (attempts.incrementAndGet() >= 5) {
                if (!captchaService.validateCaptcha(username, captcha)) {
                    log.warn("Captcha validation failed for user {}", username);
                    throw new BadCredentialsException("验证码错误");
                }
            } else if (attempts.get() == 5) {
                captchaService.generateCaptchaImage(username);
                log.info("Generated captcha for user {}", username);
                throw new BadCredentialsException("需要验证码");
            }
            log.warn("Authentication failed for user {}: invalid credentials", username);
            throw new BadCredentialsException("用户名或密码错误");
        }

        // 重置尝试计数和清除验证码
        loginAttempts.remove(username);
        captchaService.clearCaptcha(username);

        log.info("Authentication successful for user {}", username);
        return new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());
    }


    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }

} 