package com.exhibition.service;

import javax.crypto.SecretKey;
import java.util.List;

public interface JwtService {


    String generateAccessToken(String email, String userId, String userName, List<String> roles);

    String generateRefreshToken(String email);

    String generateVerificationToken(String Id ,String email,String purpose);

    String generateResetPasswordToken(String email);

    // Getter: 供其他服務調用驗證
    SecretKey getSecretKey();

    // 統一封裝 Common 驗證工具
    boolean validateToken(String token);

    String extractUserEmail(String token);

    String extractUserName(String token);

    List<String> extractRoles(String token);

    boolean validateRefreshToken(String token);
}
