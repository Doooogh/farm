package com.doooogh.farm.auth.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SocialAuthenticationService {
    
    private final UserMapper userMapper;
    private final JwtUtil jwtUtil;
    
    public TokenResponse wechatLogin(String code) {
        // 1. 获取微信用户信息
        WechatUserInfo wechatUser = getWechatUserInfo(code);
        
        // 2. 查找或创建用户
        User user = userMapper.selectOne(new QueryWrapper<User>()
            .eq("wechat_openid", wechatUser.getOpenId()));
            
        if (user == null) {
            user = createUserFromWechat(wechatUser);
        }
        
        // 3. 生成token
        return generateTokens(user);
    }
}
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
 