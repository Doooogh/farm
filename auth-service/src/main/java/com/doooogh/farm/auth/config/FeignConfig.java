package com.doooogh.farm.auth.config;

import com.doooogh.farm.auth.client.FeignErrorDecoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import feign.RequestInterceptor;
import feign.codec.ErrorDecoder;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.jackson.JacksonDecoder;
import feign.codec.Decoder;

@Configuration
public class FeignConfig {
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @Bean
    public RequestInterceptor requestInterceptor() {
        return template -> {
            template.header("X-Service-Name", "auth-service");
        };
    }
    
    @Bean
    public Decoder feignDecoder() {
        return new JacksonDecoder(objectMapper);
    }
    
    @Bean
    public ErrorDecoder errorDecoder() {
        return new FeignErrorDecoder();
    }
} 