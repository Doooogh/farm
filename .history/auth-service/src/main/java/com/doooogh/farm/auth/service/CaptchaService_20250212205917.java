package com.doooogh.farm.auth.service;

import com.google.code.kaptcha.Producer;
import com.google.code.kaptcha.util.Config;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

@Service
public class CaptchaService {

    private final Producer captchaProducer;
    private final StringRedisTemplate redisTemplate;

    public CaptchaService(StringRedisTemplate redisTemplate) {
        Properties properties = new Properties();
        properties.setProperty("kaptcha.border", "no");
        properties.setProperty("kaptcha.textproducer.font.color", "black");
        properties.setProperty("kaptcha.textproducer.char.space", "5");
        this.captchaProducer = new com.google.code.kaptcha.impl.DefaultKaptcha();
        ((com.google.code.kaptcha.impl.DefaultKaptcha) this.captchaProducer).setConfig(new Config(properties));
        this.redisTemplate = redisTemplate;
    }

    public BufferedImage generateCaptchaImage(String username) {
        String captchaText = captchaProducer.createText();
        redisTemplate.opsForValue().set(username, captchaText, 5, TimeUnit.MINUTES); // 设置5分钟过期时间
        return captchaProducer.createImage(captchaText);
    }

    public boolean validateCaptcha(String username, String captcha) {
        String storedCaptcha = redisTemplate.opsForValue().get(username);
        return storedCaptcha != null && storedCaptcha.equals(captcha);
    }

    public void clearCaptcha(String username) {
        redisTemplate.delete(username);
    }
} 