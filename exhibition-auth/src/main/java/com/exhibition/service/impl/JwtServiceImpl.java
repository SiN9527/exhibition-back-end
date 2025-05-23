package com.exhibition.service.impl;

import com.exhibition.utils.JwtUtil;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;


@Service
public class JwtServiceImpl implements JwtService {
    @Value("${jwt.secret}")
    private String secretBase64;

    @Value("${jwt.expiration}")
    private long accessTokenExpiration;

    @Value("${jwt.refreshExpiration}")
    private long refreshTokenExpiration;

    private SecretKey secretKey;

    @PostConstruct
    public void init() {
        this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretBase64));
    }

    /**
     * 產生 Access Token
     */


    @Override
    public String generateAccessToken(String email, String userId, String userName, List<String> roles) {
        return Jwts.builder()
                .setSubject(email)
                .claim("userId", userId)
                .claim("userName", userName)
                .claim("roles", roles)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + accessTokenExpiration))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * 產生 Refresh Token
     */
    @Override
    public String generateRefreshToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + refreshTokenExpiration))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * 產生驗證用 Token（預設 24 小時）
     */
    @Override
    public String generateVerificationToken(String userId ,String email,String purpose) {
        return Jwts.builder()
                .setSubject(email)
                .claim("userId", userId)
                .claim("purpose", purpose)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * 產生重設密碼 Token（30 分鐘）
     */
    @Override
    public String generateResetPasswordToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .claim("purpose", "PASSWORD_RESET")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 30 * 60 * 1000))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    // Getter: 供其他服務調用驗證
    @Override
    public SecretKey getSecretKey() {
        return secretKey;
    }

    // 統一封裝 Common 驗證工具
    @Override
    public boolean validateToken(String token) {
        return JwtUtil.validateToken(token, secretKey);
    }

    @Override
    public String extractUserEmail(String token) {
        return JwtUtil.extractSubject(token, secretKey);
    }

    @Override
    public String extractUserName(String token) {
        return JwtUtil.extractUserName(token, secretKey);

    }


    @Override
    public List<String> extractRoles(String token) {
        return JwtUtil.extractRoles(token, secretKey);
    }

    @Override
    public boolean validateRefreshToken(String token) {
        return !JwtUtil.isTokenExpired(token, secretKey);
    }
}
