package com.exhibition.utils;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.SignatureException;
import java.util.Date;
import java.util.List;
import java.util.function.Function;

/**
 * JwtUtil
 * 提供生成、解析、驗證 JWT 的靜態工具方法
 */
/**
 * JwtUtil（Common 工具）
 * 提供解析 JWT、驗證有效性、提取資訊等功能，不做簽發
 */
@Component
public class JwtUtil {
    @Value("${jwt.secret}")
    private String secretBase64;
    private SecretKey secretKey;

    @PostConstruct
    public void init() {
        this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretBase64));
    }

    /**
     * 驗證 Token 是否有效（格式正確 + 未過期）
     */
    public boolean validateToken(String token, String expectedType) {
        try {
            Claims claims = extractAllClaims(token); // 解析 Token
            return expectedType.equals(claims.get("type", String.class)) && !isTokenExpired(token);
        } catch (JwtException | IllegalArgumentException e) {
            return false; // Token 無效或已過期
        }
    }

    // **通用方法：解析 Token 內的 Claims**
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * 產生 Access Token
     */



    public boolean validateRefreshToken(String refreshToken) {
        try {
            Jws<Claims> claimsJws = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(refreshToken);

            Date expiration = claimsJws.getBody().getExpiration();
            return expiration.after(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 從 Token 擷取使用者 ID（通常是 email）
     */
    public String extractSubject(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * 從 Token 擷取 type（USER / MEMBER / ADMIN）
     */
    public String extractUserType(String token) {
        return extractClaim(token, claims -> claims.get("type", String.class));  // **提取 USER / MEMBER**
    }
    public String extractUserName(String token) {
        return extractClaim(token, claims -> claims.get("userName", String.class));
    }

    public String extractUserId(String token) {
        return extractClaim(token, claims -> claims.get("userId", String.class));
    }


    /**
     * 從 Token 擷取 type（USER / MEMBER / ADMIN）
     */
    public String extractEnable(String token) {
        return extractClaim(token, claims -> claims.get("enable", String.class));
    }



    /**
     * 從 Token 擷取 verify type
     */
    public String extractPurpose(String token) {
        return extractClaim(token, claims -> claims.get("purpose", String.class));
    }

    /**
     * 擷取角色列表（roles）
     */
    public List<String> extractRoles(String token) {
        return extractClaim(token, claims -> claims.get("roles", List.class));
    }

    /**
     * 通用方法：取得過期時間
     */
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * 判斷 Token 是否過期
     */
    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * 通用解析邏輯（由呼叫者決定要抓什麼 Claim）
     */
    public  <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claimsResolver.apply(claims);
    }

    public SecretKey getSecretKey() {
        return secretKey;
    }
}