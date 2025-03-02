package com.doooogh.farm.auth.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.doooogh.farm.auth.client.UserServiceClient;
import com.doooogh.farm.auth.dto.TokenResponse;
import com.doooogh.farm.common.auth.User;
import com.doooogh.farm.auth.model.AuthWechatUserInfo;
import com.doooogh.farm.common.util.JwtUtil;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;


/**
 * 社交登录认证服务
 * 处理微信、QQ等第三方平台的登录认证
 */
@Service
@RequiredArgsConstructor
public class SocialAuthenticationService {
    
    private final UserServiceClient userServiceClient;
    private final JwtUtil jwtUtil;
    private final WechatService wechatService;
    
    /**
     * 微信登录处理
     * 获取微信用户信息，创建或更新本地用户，生成令牌
     *
     * @param code 微信授权码
     * @return 包含访问令牌的响应
     */
    public TokenResponse wechatLogin(String code) {
        // 1. 获取微信用户信息
        AuthWechatUserInfo wechatUser = wechatService.getWechatUserInfo(code);
        // 2. 查找或创建用户
        User user = userServiceClient.findUserByIdentifier(wechatUser.getOpenId()).getData();

        if (user == null) {
            user = createUserFromWechat(wechatUser);
        }
        
        // 3. 生成访问令牌
        return TokenResponse.builder()
            .accessToken(jwtUtil.generateToken(user.getUsername()))
            .tokenType("Bearer")
            .expiresIn(jwtUtil.getAccessTokenExpiration() * 3600)
            .build();
    }
    
    /**
     * 根据微信用户信息创建本地用户
     *
     * @param wechatUser 微信用户信息
     * @return 创建的用户对象
     */
    private User createUserFromWechat(AuthWechatUserInfo wechatUser) {
        User user = new User();
        user.setUsername("wx_" + wechatUser.getOpenId());
        user.setNickname(wechatUser.getNickname());
        user.setWechatOpenid(wechatUser.getOpenId());
        user.setStatus(1);
        //todo 待补充其他信息
        userServiceClient.createUserFromWechat(user);
        return user;
    }
}
