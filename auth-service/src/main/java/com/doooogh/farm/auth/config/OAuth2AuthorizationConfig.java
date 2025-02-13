package com.doooogh.farm.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import com.doooogh.farm.auth.service.CustomClientDetailsService;

/**
 * OAuth2 授权服务器配置类。
 * 配置授权服务器的安全性、客户端详情和端点。
 */
@Configuration
@EnableAuthorizationServer
public class OAuth2AuthorizationConfig extends AuthorizationServerConfigurerAdapter {

    private final CustomClientDetailsService customClientDetailsService;

    private final JwtAccessTokenConverter jwtAccessTokenConverter;

    /**
     * 构造函数，注入自定义的 ClientDetailsService。
     *
     * @param customClientDetailsService 自定义的 ClientDetailsService 实现
     */
    public OAuth2AuthorizationConfig(CustomClientDetailsService customClientDetailsService,JwtAccessTokenConverter jwtAccessTokenConverter) {
        this.customClientDetailsService = customClientDetailsService;
        this.jwtAccessTokenConverter = jwtAccessTokenConverter;
    }

    /**
     * 配置授权服务器的安全性。
     *
     * @param security 授权服务器安全配置
     * @throws Exception 配置过程中可能抛出的异常
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security.tokenKeyAccess("permitAll()").checkTokenAccess("isAuthenticated()");
    }

    /**
     * 配置客户端详情服务。
     *
     * @param clients 客户端详情配置
     * @throws Exception 配置过程中可能抛出的异常
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.withClientDetails(customClientDetailsService);
    }

    /**
     * 配置授权服务器的端点。
     *
     * @param endpoints 授权服务器端点配置
     * @throws Exception 配置过程中可能抛出的异常
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.tokenStore(tokenStore()).accessTokenConverter(jwtAccessTokenConverter);
    }

    /**
     * 配置令牌存储。
     *
     * @return 令牌存储实现
     */
    @Bean
    public TokenStore tokenStore() {
        return new JwtTokenStore(jwtAccessTokenConverter);
    }

}