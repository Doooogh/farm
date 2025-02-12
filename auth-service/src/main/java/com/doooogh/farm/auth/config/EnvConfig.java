package com.doooogh.farm.auth.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "env")
public class EnvConfig {
    private String serverPort;
    private Database database = new Database();
    private Redis redis = new Redis();
    private Jwt jwt = new Jwt();
    private Nacos nacos = new Nacos();
    
    @Data
    public static class Database {
        private String url;
        private String username;
        private String password;
        private String driverClassName = "com.mysql.cj.jdbc.Driver";
    }
    
    @Data
    public static class Redis {
        private String host;
        private int port;
        private String password;
        private int database;
    }
    
    @Data
    public static class Jwt {
        private String secret;
        private Long tokenExpiration;
        private Long refreshTokenExpiration;
        private String issuer;
    }
    
    @Data
    public static class Nacos {
        private String serverAddr;
        private String namespace;
        private String group;
    }
} 