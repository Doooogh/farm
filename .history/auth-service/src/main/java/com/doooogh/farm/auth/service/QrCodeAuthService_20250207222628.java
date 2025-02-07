package com.doooogh.farm.auth.service;

import com.doooogh.farm.auth.exception.AuthException;
import com.doooogh.farm.auth.model.TokenResponse;
import com.doooogh.farm.auth.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 二维码扫码登录服务
 * 处理二维码生成、确认和状态检查等操作
 */
@Service
@RequiredArgsConstructor
public class QrCodeAuthService {
    
    private final RedisUtil redisUtil;
    
    /**
     * 生成二维码标识
     * 创建唯一标识并存入Redis，设置过期时间
     *
     * @return 二维码标识
     */
    public String generateQrCode() {
        String qrCodeId = UUID.randomUUID().toString();
        redisUtil.set("qrcode:" + qrCodeId, "WAITING", 5, TimeUnit.MINUTES);
        return qrCodeId;
    }
    
    /**
     * 确认二维码登录
     * 移动端扫码后调用此方法确认登录
     *
     * @param qrCodeId 二维码标识
     * @param userId 用户ID
     */
    public void confirmLogin(String qrCodeId, String userId) {
        redisUtil.set("qrcode:" + qrCodeId, userId, 1, TimeUnit.MINUTES);
    }
    
    /**
     * 检查二维码状态
     * Web端轮询调用此方法检查登录状态
     *
     * @param qrCodeId 二维码标识
     * @return 登录状态和令牌信息
     * @throws AuthException 当二维码过期时抛出
     */
    public TokenResponse checkQrCodeStatus(String qrCodeId) {
        String status = redisUtil.get("qrcode:" + qrCodeId);
        if (status == null) {
            throw new AuthException(401003, "二维码已过期");
        }
        if ("WAITING".equals(status)) {
            return TokenResponse.builder().status("WAITING").build();
        }
        // 生成访问令牌
        return generateTokens(status);
    }
} 