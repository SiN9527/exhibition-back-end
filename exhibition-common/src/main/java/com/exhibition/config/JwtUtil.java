package com.exhibition.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Component // Spring Bean，方便依賴注入
public class JwtUtil {

    private final SecretKey secretKey; // JWT 簽名密鑰
    private final long expirationMillis; // JWT 過期時間
    private final long refreshExpirationMillis; // Refresh Token 過期時間

    // 從 application.properties 讀取密鑰和過期時間
    public JwtUtil(@Value("${jwt.secret}") String secret, @Value("${jwt.expiration}") long expirationMillis, @Value("${jwt.refreshExpiration}") long refreshExpirationMillis) {
        this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret)); // Base64 解碼密鑰
        this.expirationMillis = expirationMillis;
        this.refreshExpirationMillis = refreshExpirationMillis;
    }

    // **生成 JWT Token**
    public String generateToken(String email, String type, List<String> roles) {
        return Jwts.builder()
                .setSubject(email) // 設定 Token 的持有者 (用戶名)
                .claim("roles", roles) // 儲存使用者角色資訊
                .claim("type", type) // 記錄該 Token 屬於 `USER` 還是 `MEMBER`
                .setIssuedAt(new Date()) // 設定發行時間
                .setExpiration(new Date(System.currentTimeMillis() + expirationMillis)) // 設定過期時間
                .signWith(secretKey, SignatureAlgorithm.HS256) // 使用 HMAC-SHA256 簽名
                .compact(); // 生成 JWT Token 字串
    }

    // **驗證 Token 是否有效 & 是否屬於指定身份**
    public boolean validateToken(String token, String expectedType) {
        try {
            Claims claims = extractAllClaims(token); // 解析 Token
            return expectedType.equals(claims.get("type", String.class)) && !isTokenExpired(token);
        } catch (Exception e) {
            return false; // Token 無效或已過期
        }
    }

    public String extractUserType(String token) {
        return extractClaim(token, claims -> claims.get("type", String.class));  // **提取 USER / MEMBER**
    }

    // **解析 Token 取得用戶名**
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // **解析 Token 取得角色列表**
    public List<String> extractRoles(String token) {
        return extractClaim(token, claims -> claims.get("roles", List.class));
    }

    // **通用方法：解析 Token 內的 Claims**
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // **通用方法：提取特定 Claim**
    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        return claimsResolver.apply(extractAllClaims(token));
    }

    // **判斷 Token 是否過期**
    public boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }

    /**
     * 產生 Email 驗證 Token（24 小時有效）
     *
     * @param email 會員 Email
     * @return JWT Token
     */
    public String generateVerificationToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .claim("purpose", "EMAIL_VERIFICATION") // 標示用途
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000)) // 24 小時有效
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * 產生密碼重設 Token（30 分鐘有效）
     *
     * @param email 會員 Email
     * @return JWT Token
     */
    public String generatePasswordResetToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .claim("purpose", "PASSWORD_RESET") // 標示用途
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 30 * 60 * 1000)) // 30 分鐘有效
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * 解析 Token 取得 Email
     */
    public String extractEmail(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    // **驗證 JWT 並從 Repository 取得對應的使用者**
    public <T> Optional<T> validateAndGetEntity(UserDetails userDetails, JpaRepository<T, String> repository) {

        String email = userDetails.getUsername() ;

        //  Repository 查找對應的使用者
        return repository.findAll().stream()
                .filter(user -> {
                    try {
                        return (user.getClass().getMethod("getEmail").invoke(user)).equals(email);
                    } catch (Exception e) {
                        throw new RuntimeException("User entity does not have getEmail method");
                    }
                })
                .findFirst();
  }

    public String generateRefreshToken(String email) {

        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + refreshExpirationMillis)) // **設定 Refresh Token 有效期**
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateAdminRefreshToken(String account) {

        return Jwts.builder()
                .setSubject(account)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + refreshExpirationMillis)) // **設定 Refresh Token 有效期**
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

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
//    public boolean validateRefreshToken(String refreshToken) {
//        try {
//            Claims claims = Jwts.parserBuilder()
//                    .setSigningKey(secretKey) // 使用相同密鑰驗證
//                    .build()
//                    .parseClaimsJws(refreshToken)
//                    .getBody();
//
//            // 確保 Token 沒有過期
//            return !claims.getExpiration().before(new Date());
//        } catch (JwtException | IllegalArgumentException e) {
//            return false; // 無效的 Token
//        }
//    }

    public String generateAccessToken(String email, String type, List<String> roles) {
        return Jwts.builder()
                .setSubject(email) // 設定 Token 的持有者 (用戶名)
                .claim("roles", roles) // 儲存使用者角色資訊
                .claim("type", type) // 記錄該 Token 屬於 `USER` 還是 `MEMBER`
                .setIssuedAt(new Date()) // 設定簽發時間
                .setExpiration(new Date(System.currentTimeMillis() + expirationMillis)) // 設定過期時間
                .signWith(secretKey, SignatureAlgorithm.HS256) // 使用 HS256 簽名
                .compact();
    }

    public String generateAdminAccessToken(String account, String type, List<String> roles) {
        return Jwts.builder()
                .setSubject(account) // 設定 Token 的持有者 (用戶名)
                .claim("roles", roles) // 儲存使用者角色資訊
                .claim("type", type) // 記錄該 Token 屬於 `USER` 還是 `MEMBER`
                .setIssuedAt(new Date()) // 設定簽發時間
                .setExpiration(new Date(System.currentTimeMillis() + expirationMillis)) // 設定過期時間
                .signWith(secretKey, SignatureAlgorithm.HS256) // 使用 HS256 簽名
                .compact();
    }
}
