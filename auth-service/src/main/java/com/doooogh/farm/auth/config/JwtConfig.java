package com.doooogh.farm.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.jwt.crypto.sign.RsaSigner;
import org.springframework.security.jwt.crypto.sign.RsaVerifier;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import java.io.File;
import java.nio.file.Files;
import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

/**
 * @author Li m13283354149@163.com
 * @date 2025/02/13
 * @description
 */
@Configuration
public class JwtConfig {
        // 读取私钥
        private RSAPrivateKey privateKey() throws Exception {
                File privateKeyFile = new File("private.key");
                byte[] keyBytes = Files.readAllBytes(privateKeyFile.toPath());
                KeyFactory keyFactory = KeyFactory.getInstance("RSA");
                return (RSAPrivateKey) keyFactory.generatePrivate(new java.security.spec.PKCS8EncodedKeySpec(keyBytes));
        }

        // 读取公钥
        private RSAPublicKey publicKey() throws Exception {
                File publicKeyFile = new File("public.key");
                byte[] keyBytes = Files.readAllBytes(publicKeyFile.toPath());
                KeyFactory keyFactory = KeyFactory.getInstance("RSA");
                return (RSAPublicKey) keyFactory.generatePublic(new java.security.spec.X509EncodedKeySpec(keyBytes));
        }



        /**
         * 配置 JWT 访问令牌转换器。
         *
         * @return JWT 访问令牌转换器
         */
        @Bean
        public JwtAccessTokenConverter accessTokenConverter() throws Exception {
                JwtAccessTokenConverter converter = new JwtAccessTokenConverter();

                // 使用私钥来签署 JWT
                converter.setSigner(new RsaSigner(privateKey()));  // 设置私钥
                return converter;
        }
}
