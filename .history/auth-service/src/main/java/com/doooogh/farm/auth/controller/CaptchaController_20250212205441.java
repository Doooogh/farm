package com.doooogh.farm.auth.controller;

import com.doooogh.farm.auth.security.CustomAuthenticationProvider;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CaptchaController {

    private final CustomAuthenticationProvider customAuthenticationProvider;

    public CaptchaController(CustomAuthenticationProvider customAuthenticationProvider) {
        this.customAuthenticationProvider = customAuthenticationProvider;
    }

    /**
     * 生成验证码并返回给前端。
     *
     * @param username 用户名
     * @return 验证码
     */
    @GetMapping("/auth/captcha")
    public ResponseEntity<String> getCaptcha(@RequestParam String username) {
        String captcha = customAuthenticationProvider.generateCaptcha(username);
        return ResponseEntity.ok(captcha);
    }
} 