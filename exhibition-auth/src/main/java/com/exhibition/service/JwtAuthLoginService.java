package com.exhibition.service;

import com.svc.ems.dto.auth.AdminLoginRequest;
import com.svc.ems.dto.auth.LoginRequest;
import com.svc.ems.dto.base.ApiResponseTemplate;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;

public interface JwtAuthLoginService {

    public ResponseEntity<ApiResponseTemplate<String>> memberAuthLogin(LoginRequest req, HttpServletResponse response);

    public ResponseEntity<ApiResponseTemplate<String>> adminAuthLogin(AdminLoginRequest req, HttpServletResponse response);


}
