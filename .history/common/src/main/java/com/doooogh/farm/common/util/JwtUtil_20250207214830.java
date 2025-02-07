package com.doooogh.farm.common.util;

import com.doooogh.farm.common.config.JwtConfig;
import com.doooogh.farm.common.exception.AuthException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class JwtUtil {

    private final JwtConfig jwtConfig;

    /**
     * 生成token
     */
    public String generateToken(UserDetails userDetails, boolean isRefreshToken) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", userDetails.getUsername());
        claims.put("authorities", userDetails.getAuthorities());
        claims.put("type", isRefreshToken ? "refresh_token" : "access_token");
        
        long expiration = isRefreshToken ? 
            jwtConfig.getRefreshTokenExpiration() * 24 * 60 * 60 * 1000 : 
            jwtConfig.getTokenExpiration() * 60 * 60 * 1000;
            
        return Jwts.builder()
            .setClaims(claims)
            .setSubject(userDetails.getUsername())
            .setIssuer(jwtConfig.getIssuer())
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + expiration))
            .signWith(getSigningKey())
            .compact();
    }

    /**
     * 从token中获取用户名
     */
    public String getUsernameFromToken(String token) {
        try {
            Claims claims = getClaimsFromToken(token);
            return claims.getSubject();
        } catch (ExpiredJwtException e) {
            throw AuthException.tokenExpired();
        } catch (Exception e) {
            throw AuthException.invalidToken();
        }
    }

    /**
     * 验证token
     */
    public boolean validateToken(String token, UserDetails userDetails) {
        try {
            String username = getUsernameFromToken(token);
            Claims claims = getClaimsFromToken(token);
            boolean isTokenExpired = claims.getExpiration().before(new Date());
            return (username.equals(userDetails.getUsername()) && !isTokenExpired);
        } catch (ExpiredJwtException e) {
            throw AuthException.tokenExpired();
        } catch (Exception e) {
            throw AuthException.invalidToken();
        }
    }

    /**
     * 判断token是否可以被刷新
     */
    public boolean canTokenBeRefreshed(String token) {
        try {
            Claims claims = getClaimsFromToken(token);
            String tokenType = (String) claims.get("type");
            return "refresh_token".equals(tokenType);
        } catch (ExpiredJwtException e) {
            return false;
        }
    }

    /**
     * 从token中获取Claims
     */
    private Claims getClaimsFromToken(String token) {
        return Jwts.parserBuilder()
            .setSigningKey(getSigningKey())
            .build()
            .parseClaimsJws(token)
            .getBody();
    }

    /**
     * 获取签名密钥
     */
    private SecretKey getSigningKey() {
        byte[] keyBytes = jwtConfig.getSecret().getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }
} 