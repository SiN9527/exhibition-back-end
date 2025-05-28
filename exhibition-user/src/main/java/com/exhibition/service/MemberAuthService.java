package com.exhibition.service;

import com.exhibition.dto.ApiResponseTemplate;
import com.exhibition.dto.auth.AdminLoginRequest;
import com.exhibition.dto.auth.MemberRegisterRequest;
import com.exhibition.dto.auth.VerificationRequest;
import com.exhibition.dto.user.*;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

public interface MemberAuthService {


    // 註冊
    public String memberRegister(MemberRegisterRequest req);


    String verifyEmail(@RequestBody VerificationRequest req, HttpServletResponse response);


    String memberResetPasswordAfterLogin(MemberResetPwdRequest req, UserDetails userDetails, HttpServletResponse response);

    @Transactional
    String memberUpdateEmail(MemberEmailUpdateRequest req, UserDetails userDetails);

    // 登出
    public String memberLogout(HttpServletResponse response);

    // 更新Token
//    public String memberRefreshToken(String refreshToken, HttpServletResponse response);
//
//
//    public String memberUpdatePwd(MemberPwdUpdateRequest req, UserDetails userDetails, HttpServletResponse response);

    // **忘記密碼 API**
    String memberForgotPwd(MemberPwdUpdateRequest req);


}
