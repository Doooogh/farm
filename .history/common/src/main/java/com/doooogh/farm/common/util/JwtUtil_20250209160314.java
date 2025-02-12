package com.doooogh.farm.common.util;

import com.doooogh.farm.common.config.JwtConfig;
import com.doooogh.farm.common.exception.AuthException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * JWT工具类
 * 提供JWT令牌的生成、解析和验证功能
 */
@Component
@RequiredArgsConstructor
public class JwtUtil {

    private final JwtConfig jwtConfig;

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    /**
     * 生成JWT令牌
     * 根据用户信息和令牌类型生成对应的JWT令牌
     *
     * @param userDetails 用户详情信息
     * @param isRefreshToken 是否为刷新令牌
     * @return JWT令牌字符串
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
     * 从令牌中获取用户名
     *
     * @param token JWT令牌
     * @return 用户名
     * @throws AuthException 当令牌无效或过期时抛出
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
     * 验证令牌有效性
     *
     * @param token JWT令牌
     * @param userDetails 用户详情信息
     * @return 令牌是否有效
     * @throws AuthException 当令牌无效或过期时抛出
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
     * 判断令牌是否可以被刷新
     *
     * @param token JWT令牌
     * @return 是否可以刷新
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
     * 从令牌中获取Claims信息
     *
     * @param token JWT令牌
     * @return Claims对象
     * @throws JwtException 当令牌解析失败时抛出
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
     *
     * @return 用于签名的密钥对象
     */
    private SecretKey getSigningKey() {
        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userDetails.getUsername());
    }

    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration * 1000))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }
} 