package com.doooogh.farm.auth.service;

import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class SmsService {

    private final ConcurrentHashMap<String, String> smsCodeStore = new ConcurrentHashMap<>();

    public void sendSmsCode(String phoneNumber) {
        String code = String.format("%06d", new Random().nextInt(999999));
        // 调用短信服务提供商的API发送短信
        // 例如：Twilio API
        // Message.creator(new PhoneNumber(phoneNumber), new PhoneNumber("YourTwilioNumber"), "Your code is: " + code).create();
        smsCodeStore.put(phoneNumber, code);
        // 设置验证码过期时间
        // 使用 Redis 或其他持久化存储来实现过期机制
    }

    public boolean validateSmsCode(String phoneNumber, String code) {
        String storedCode = smsCodeStore.get(phoneNumber);
        return storedCode != null && storedCode.equals(code);
    }

    public void clearSmsCode(String phoneNumber) {
        smsCodeStore.remove(phoneNumber);
    }
} 