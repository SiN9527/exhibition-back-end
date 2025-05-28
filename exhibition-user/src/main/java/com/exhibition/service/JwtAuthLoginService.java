package com.exhibition.service;


import com.exhibition.dto.ApiResponseTemplate;
import com.exhibition.dto.auth.AdminLoginRequest;
import com.exhibition.dto.auth.LoginRequest;
import com.exhibition.dto.auth.MemberLoginRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;

public interface JwtAuthLoginService {

    String  memberAuthLogin(MemberLoginRequest req, HttpServletResponse response);

    public String adminAuthLogin(AdminLoginRequest req, HttpServletResponse response);


}
