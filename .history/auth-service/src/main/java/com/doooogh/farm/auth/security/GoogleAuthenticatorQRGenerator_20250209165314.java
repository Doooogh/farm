package com.doooogh.farm.auth.security;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class GoogleAuthenticatorQRGenerator {
    
    public static String getOtpAuthURL(String issuer, String accountName, String secretKey) {
        try {
            return String.format("otpauth://totp/%s:%s?secret=%s&issuer=%s",
                URLEncoder.encode(issuer, StandardCharsets.UTF_8.toString()),
                URLEncoder.encode(accountName, StandardCharsets.UTF_8.toString()),
                URLEncoder.encode(secretKey, StandardCharsets.UTF_8.toString()),
                URLEncoder.encode(issuer, StandardCharsets.UTF_8.toString()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
} 