package com.doooogh.farm.auth.service;

import com.doooogh.farm.auth.model.AuthWechatUserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class WechatService {
    
    private final RestTemplate restTemplate;
    
    public AuthWechatUserInfo getWechatUserInfo(String code) {
        // TODO: 实现微信用户信息获取逻辑
        return new AuthWechatUserInfo();
    }
} 