package com.exhibition.service;

import javax.crypto.SecretKey;
import java.util.List;

public interface JwtService {


    String generateAccessToken(String email, String userId, String type, List<String> roles);

    String generateRefreshToken(String email);

    String generateVerificationToken(String id ,String email,String purpose);

    String generateResetPasswordToken(String email);

}
