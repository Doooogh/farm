package com.doooogh.farm.auth.config.token;

import com.doooogh.farm.common.util.JwtUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.DefaultOAuth2RefreshToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author Li m13283354149@163.com
 * @date 2025/02/23
 * @description
 */
@Data
public class CustomTokenService extends DefaultTokenServices {


    private final JwtUtil jwtUtil;
    private final  TokenStore tokenStore;

    private final static String SIGNING_KEY = "bxc123";


    public CustomTokenService(JwtUtil jwtUtil, TokenStore tokenStore) {
        this.jwtUtil = jwtUtil;
        this.tokenStore = tokenStore;
    }

    @Override
    public OAuth2AccessToken createAccessToken(OAuth2Authentication authentication) throws AuthenticationException {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String token = jwtUtil.generateToken(userDetails, false);
        String refreshToken = jwtUtil.generateToken(userDetails, true);


        // 3️⃣ 封装 Token
        DefaultOAuth2AccessToken accessToken = new DefaultOAuth2AccessToken(token);
        accessToken.setExpiration(new Date(System.currentTimeMillis() + jwtUtil.getExpiration())); // 1 小时过期
        accessToken.setRefreshToken(new DefaultOAuth2RefreshToken(refreshToken));


        // 4️⃣ 存储 Token
        tokenStore.storeAccessToken(accessToken, authentication);
        return accessToken;

    }


}
