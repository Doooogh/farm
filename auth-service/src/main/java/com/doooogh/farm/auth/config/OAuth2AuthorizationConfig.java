package com.doooogh.farm.auth.config;

import org.bouncycastle.openssl.PEMParser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import com.doooogh.farm.auth.service.CustomClientDetailsService;

import java.io.FileReader;
import java.nio.file.Files;
import java.security.interfaces.RSAPrivateKey;

/**
 * OAuth2 授权服务器配置类。
 * 配置授权服务器的安全性、客户端详情和端点。
 */
@Configuration
@EnableAuthorizationServer
public class OAuth2AuthorizationConfig extends AuthorizationServerConfigurerAdapter {

    private final CustomClientDetailsService customClientDetailsService;


    /**
     * 构造函数，注入自定义的 ClientDetailsService。
     * authenticationManager
     *
     * @param customClientDetailsService 自定义的 ClientDetailsService 实现
     */
    public OAuth2AuthorizationConfig(CustomClientDetailsService customClientDetailsService) {
        this.customClientDetailsService = customClientDetailsService;
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
        endpoints.tokenStore(tokenStore()).accessTokenConverter(accessTokenConverter());
    }

    /**
     * 配置令牌存储。
     *
     * @return 令牌存储实现
     */
    @Bean
    public TokenStore tokenStore() throws Exception {
        return new JwtTokenStore(accessTokenConverter());
    }


    /**
     * 配置 JWT 访问令牌转换器。
     *
     * @return JWT 访问令牌转换器
     */
    @Bean
    public JwtAccessTokenConverter accessTokenConverter() throws Exception {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();

        // 读取并设置私钥 (PEM 格式的私钥)
        ClassPathResource resource = new ClassPathResource("private.pem");
        String privateKey = new String(Files.readAllBytes(resource.getFile().toPath()));

        // 去除头尾的非私钥部分
        privateKey = privateKey.replace("-----BEGIN PRIVATE KEY-----", "").replace("-----END PRIVATE KEY-----", "").replaceAll("\n", "");

        // 设置签名密钥
        converter.setSigningKey(privateKey);  // 这里不需要设置验证密钥

        return converter;
    }


}