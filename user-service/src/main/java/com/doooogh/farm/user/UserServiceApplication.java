package com.doooogh.farm.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@EnableDiscoveryClient
@EnableFeignClients
@SpringBootApplication
@ComponentScan(basePackages = {"com.doooogh.farm.user", "com.doooogh.farm.common"})
public class UserServiceApplication {
    // ... existing code ...

    public static void main(String[] args) {
        SpringApplication.run(UserServiceApplication.class, args);
    }
} 