package com.exhibition.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;

import javax.crypto.SecretKey;
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
public class JwtUtil {

    /**
     * 驗證 Token 是否有效（格式正確 + 未過期）
     */
    public static boolean validateToken(String token, SecretKey secretKey) {
        try {
            Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * 從 Token 擷取使用者 ID（通常是 email）
     */
    public static String extractSubject(String token, SecretKey secretKey) {
        return extractClaim(token, Claims::getSubject, secretKey);
    }

    /**
     * 從 Token 擷取 type（USER / MEMBER / ADMIN）
     */
    public static String extractUserName(String token, SecretKey secretKey) {
        return extractClaim(token, claims -> claims.get("userName", String.class), secretKey);
    }

    public static String extractUserId(String token, SecretKey secretKey) {
        return extractClaim(token, claims -> claims.get("userId", String.class), secretKey);
    }


    /**
     * 從 Token 擷取 type（USER / MEMBER / ADMIN）
     */
    public static String extractEnable(String token, SecretKey secretKey) {
        return extractClaim(token, claims -> claims.get("enal", String.class), secretKey);
    }



    /**
     * 從 Token 擷取 verify type
     */
    public static String extractPurpose(String token, SecretKey secretKey) {
        return extractClaim(token, claims -> claims.get("purpose", String.class), secretKey);
    }

    /**
     * 擷取角色列表（roles）
     */
    public static List<String> extractRoles(String token, SecretKey secretKey) {
        return extractClaim(token, claims -> claims.get("roles", List.class), secretKey);
    }

    /**
     * 通用方法：取得過期時間
     */
    public static Date extractExpiration(String token, SecretKey secretKey) {
        return extractClaim(token, Claims::getExpiration, secretKey);
    }

    /**
     * 判斷 Token 是否過期
     */
    public static boolean isTokenExpired(String token, SecretKey secretKey) {
        return extractExpiration(token, secretKey).before(new Date());
    }

    /**
     * 通用解析邏輯（由呼叫者決定要抓什麼 Claim）
     */
    public static <T> T extractClaim(String token, Function<Claims, T> claimsResolver, SecretKey secretKey) {
        final Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claimsResolver.apply(claims);
    }
}