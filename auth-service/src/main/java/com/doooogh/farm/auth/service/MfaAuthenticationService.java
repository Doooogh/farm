package com.doooogh.farm.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MfaAuthenticationService {
    
    private final GoogleAuthenticator googleAuthenticator;
    private final UserService userService;
    
    public boolean verifyMfaCode(String username, String code) {
        User user = userService.getByUsername(username);
        String secretKey = user.getMfaSecretKey();
        
        return googleAuthenticator.authorize(secretKey, Integer.parseInt(code));
    }
    
    public String generateMfaSecret() {
        return googleAuthenticator.createCredentials().getKey();
    }
    
    public String generateQrCodeUrl(String username, String secretKey) {
        return GoogleAuthenticatorQRGenerator.getOtpAuthURL(
            "YourApp", username, new GoogleAuthenticatorKey(secretKey)
        );
    }
} 