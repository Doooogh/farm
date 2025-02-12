package com.doooogh.farm.auth.security;

import org.apache.commons.codec.binary.Base32;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

@Component
public class GoogleAuthenticator {
    
    private static final int SECRET_SIZE = 10;
    private static final String SEED = "g8GjEvTbW5oVSV7avLBdwIHqGlUYNzKFI7izOF8GwLDVKs2m0QN7vxRs2im5MDaNCWGmcD2rvcZx";
    private static final String RANDOM_NUMBER_ALGORITHM = "SHA1PRNG";
    
    public String createCredentials() {
        byte[] buffer = new byte[SECRET_SIZE];
        new SecureRandom().nextBytes(buffer);
        Base32 codec = new Base32();
        return codec.encodeToString(buffer);
    }
    
    public boolean authorize(String secret, int code) {
        Base32 codec = new Base32();
        byte[] decodedKey = codec.decode(secret);
        long currentTime = System.currentTimeMillis() / 30000;
        
        try {
            return checkCode(decodedKey, currentTime, code);
        } catch (Exception e) {
            return false;
        }
    }
    
    private boolean checkCode(byte[] key, long t, int code) 
            throws NoSuchAlgorithmException, InvalidKeyException {
        byte[] data = new byte[8];
        long value = t;
        for (int i = 8; i-- > 0; value >>>= 8) {
            data[i] = (byte) value;
        }
        
        SecretKeySpec signKey = new SecretKeySpec(key, "HmacSHA1");
        Mac mac = Mac.getInstance("HmacSHA1");
        mac.init(signKey);
        byte[] hash = mac.doFinal(data);
        
        int offset = hash[hash.length - 1] & 0xF;
        long truncatedHash = 0;
        for (int i = 0; i < 4; ++i) {
            truncatedHash <<= 8;
            truncatedHash |= (hash[offset + i] & 0xFF);
        }
        
        truncatedHash &= 0x7FFFFFFF;
        truncatedHash %= 1000000;
        
        return truncatedHash == code;
    }
} 