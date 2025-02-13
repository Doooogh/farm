package com.doooogh.farm.auth.service;

import com.google.code.kaptcha.Producer;
import com.google.code.kaptcha.util.Config;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

@Service
public class CaptchaService {

    private final Producer captchaProducer;
    private final RedisUtil redisUtil;

    public CaptchaService(RedisUtil redisUtil) {
        Properties properties = new Properties();
        properties.setProperty("kaptcha.border", "no");
        properties.setProperty("kaptcha.textproducer.font.color", "black");
        properties.setProperty("kaptcha.textproducer.char.space", "5");
        this.captchaProducer = new com.google.code.kaptcha.impl.DefaultKaptcha();
        ((com.google.code.kaptcha.impl.DefaultKaptcha) this.captchaProducer).setConfig(new Config(properties));
        this.redisUtil = redisUtil;
    }

    public BufferedImage generateCaptchaImage(String username) {
        String captchaText = captchaProducer.createText();
        redisUtil.set(username, captchaText, 5, TimeUnit.MINUTES);
        return captchaProducer.createImage(captchaText);
    }

    public boolean validateCaptcha(String username, String captcha) {
        String storedCaptcha = (String) redisUtil.get(username);
        return storedCaptcha != null && storedCaptcha.equals(captcha);
    }

    public void clearCaptcha(String username) {
        redisUtil.delete(username);
    }
} 