package com.doooogh.farm.auth.service;

import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class CaptchaService {

    private final ConcurrentHashMap<String, String> captchaStore = new ConcurrentHashMap<>();

    public String generateCaptcha(String username) {
        String captcha = String.valueOf(new Random().nextInt(9999));
        captchaStore.put(username, captcha);
        return captcha;
    }

    public boolean validateCaptcha(String username, String captcha) {
        String storedCaptcha = captchaStore.get(username);
        return storedCaptcha != null && storedCaptcha.equals(captcha);
    }

    public void clearCaptcha(String username) {
        captchaStore.remove(username);
    }
} 