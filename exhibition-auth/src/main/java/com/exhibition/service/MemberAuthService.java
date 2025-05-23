package com.exhibition.service;

import com.exhibition.dto.ApiResponseTemplate;
import com.exhibition.dto.auth.AdminLoginRequest;
import com.exhibition.dto.auth.MemberRegisterRequest;
import com.exhibition.dto.user.*;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

public interface MemberAuthService {


    // 註冊
    public ResponseEntity<ApiResponseTemplate<String>> memberRegister(MemberRegisterRequest req);

    // 驗證
    public ResponseEntity<ApiResponseTemplate<String>> verifyEmail(@RequestBody Map<String, String> token, HttpServletResponse response);

    // cookie取得資料
    public ResponseEntity<ApiResponseTemplate<MemberProfileResponse>> memberGetProfile(UserDetails userDetails);

    // 忘記密碼
    public ResponseEntity<ApiResponseTemplate<?>> memberFindPwd(AdminLoginRequest req);

    ResponseEntity<ApiResponseTemplate<?>> memberResetPasswordAfterLogin(MemberResetPwdRequest req, UserDetails userDetails, HttpServletResponse response);

    // 登出
    public ResponseEntity<ApiResponseTemplate<?>> memberLogout(HttpServletResponse response);

    ResponseEntity<ApiResponseTemplate<?>> memberUpdateEmail(MemberEmailUpdateRequest req, UserDetails userDetails);

    // 更新Token
    public ResponseEntity<ApiResponseTemplate<?>> memberRefreshToken(String refreshToken, HttpServletResponse response);

    // 更新個人資料
    public ResponseEntity<ApiResponseTemplate<?>> memberUpdateProfile(MemberUpdateRequest req, UserDetails userDetails, HttpServletResponse response);

    public ResponseEntity<ApiResponseTemplate<?>> memberUpdatePwd(MemberPwdUpdateRequest req, UserDetails userDetails, HttpServletResponse response);

    // **忘記密碼 API**
    ResponseEntity<ApiResponseTemplate<?>> memberForgotPwd(MemberPwdUpdateRequest req);

//    public ResponseEntity<ApiResponseTemplate<?>> memberResetPwd(MemberResetPwdRequest req, UserDetails userDetails);


}
