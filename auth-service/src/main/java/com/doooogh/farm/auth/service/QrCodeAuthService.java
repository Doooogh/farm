package com.doooogh.farm.auth.service;

import com.doooogh.farm.auth.dto.TokenResponse;
import com.doooogh.farm.common.auth.User;
import com.doooogh.farm.common.exception.AuthException;
import com.doooogh.farm.common.util.RedisUtil;
import com.doooogh.farm.common.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import com.doooogh.farm.common.exception.BusinessException;

/**
 * 二维码扫码登录服务
 * 处理二维码生成、确认和状态检查等操作
 */
@Service
@RequiredArgsConstructor
public class QrCodeAuthService {
    
    private final RedisUtil redisUtil;
    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;
    
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
        String status = (String) redisUtil.get("qrcode:" + qrCodeId);
        if (status == null) {
            throw BusinessException.invalidParameter("二维码已过期");
        }
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
        String status = (String) redisUtil.get("qrcode:" + qrCodeId);
        if (status == null) {
            throw BusinessException.invalidParameter("二维码已过期");
        }
        if ("WAITING".equals(status)) {
            return TokenResponse.builder().status("WAITING").build();
        }
        
        // 生成访问令牌
        User user = null;
        if (user == null) {
            throw BusinessException.notFound("用户不存在");
        }
        
        // 加载用户详情
        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());
        
        // 生成令牌
        String accessToken = jwtUtil.generateToken(userDetails, false);
        String refreshToken = jwtUtil.generateToken(userDetails, true);
        
        return TokenResponse.builder()
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .tokenType("Bearer")
            .expiresIn(jwtUtil.getAccessTokenExpiration() * 3600)
            .build();
    }
} 