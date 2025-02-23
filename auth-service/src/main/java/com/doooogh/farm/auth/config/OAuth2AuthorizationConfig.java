package com.doooogh.farm.auth.config;

import com.doooogh.farm.auth.config.token.CustomTokenService;
import com.doooogh.farm.auth.filter.CustomAuthenticationFilter;
import com.doooogh.farm.auth.service.CustomUserDetailsService;
import com.doooogh.farm.common.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.bouncycastle.openssl.PEMParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.InMemoryAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.*;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import com.doooogh.farm.auth.service.CustomClientDetailsService;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

import java.io.FileReader;
import java.nio.file.Files;
import java.security.interfaces.RSAPrivateKey;
import java.util.ArrayList;
import java.util.List;

/**
 * OAuth2 授权服务器配置类。
 * 配置授权服务器的安全性、客户端详情和端点。
 */
@Configuration
@EnableAuthorizationServer
@RequiredArgsConstructor
public class OAuth2AuthorizationConfig extends AuthorizationServerConfigurerAdapter {

    private final CustomUserDetailsService customUserDetailsService;

    private final JwtAccessTokenConverter accessTokenConverter;


    private final AuthenticationManager authenticationManager;


    private final TokenStore tokenStore;

    private final JwtUtil jwtUtil;


    /**
     * 配置授权服务器的安全性。
     *
     * @param security 授权服务器安全配置
     * @throws Exception 配置过程中可能抛出的异常
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security
                      .tokenKeyAccess("permitAll()")                    //oauth/token_key是公开
                .checkTokenAccess("permitAll()")  ;
    }

    /**
     * 配置客户端详情服务。
     *
     * @param clients 客户端详情配置
     * @throws Exception 配置过程中可能抛出的异常
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()// 使用in‐memory存储
                .withClient("c1")// client_id
                .secret(new BCryptPasswordEncoder().encode("secret"))
                .resourceIds("res1")
                .authorizedGrantTypes("authorization_code", "password", "client_credentials", "implicit", "refresh_token")// 该client允许的授权类型 authorization_code,password,refresh_token,implicit,client_credentials
                .scopes("all")// 允许的授权范围
                .autoApprove(false) //加上验证回调地址
                .authorities("admin")
                .redirectUris("http://www.baidu.com");
    }

    /**
     * @description: 设置授权码模式的授权码如何存取，暂时采用内存方式
     * @author Li
     * @date 2025/2/23
     */
    @Bean
    public AuthorizationCodeServices authorizationCodeServices() {
        InMemoryAuthorizationCodeServices inMemoryAuthorizationCodeServices = new InMemoryAuthorizationCodeServices();
        return inMemoryAuthorizationCodeServices;
    }


    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
        endpoints
                .authenticationManager(authenticationManager)//认证管理器
                .userDetailsService(customUserDetailsService)
                .tokenServices(new CustomTokenService(jwtUtil, tokenStore))//令牌管理服务
                .accessTokenConverter(accessTokenConverter)
                .authorizationCodeServices(authorizationCodeServices())//授权码服务
                .allowedTokenEndpointRequestMethods(HttpMethod.POST);
    }


}