package com.doooogh.farm.auth.service;

import com.doooogh.farm.auth.exception.AuthException;
import com.doooogh.farm.auth.model.TokenResponse;
import com.doooogh.farm.auth.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class QrCodeAuthService {
    
    private final RedisUtil redisUtil;
    
    public String generateQrCode() {
        String qrCodeId = UUID.randomUUID().toString();
        redisUtil.set("qrcode:" + qrCodeId, "WAITING", 5, TimeUnit.MINUTES);
        return qrCodeId;
    }
    
    public void confirmLogin(String qrCodeId, String userId) {
        redisUtil.set("qrcode:" + qrCodeId, userId, 1, TimeUnit.MINUTES);
    }
    
    public TokenResponse checkQrCodeStatus(String qrCodeId) {
        String status = redisUtil.get("qrcode:" + qrCodeId);
        if (status == null) {
            throw new AuthException(401003, "二维码已过期");
        }
        if ("WAITING".equals(status)) {
            return TokenResponse.builder().status("WAITING").build();
        }
        // 生成token
        return generateTokens(status);
    }
} 