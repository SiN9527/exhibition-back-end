package com.exhibition.controller;


import com.exhibition.dto.ApiResponseTemplate;
import com.exhibition.dto.auth.AdminLoginRequest;
import com.exhibition.dto.auth.AdminRegisterRequest;
import com.exhibition.service.AdminAuthService;
import com.exhibition.service.JwtAuthLoginService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/sys/admin")
public class AdminSysController {


    private final AdminAuthService adminAuthService;
    private final JwtAuthLoginService jwtAuthLoginService;

    public AdminSysController(AdminAuthService adminAuthService, JwtAuthLoginService jwtAuthLoginService) {
        this.adminAuthService = adminAuthService;

        this.jwtAuthLoginService = jwtAuthLoginService;
    }

    @PostMapping("/login")
    @Operation(summary = "管理員登入")
    public ResponseEntity<ApiResponseTemplate<String>> authLogin( @RequestBody AdminLoginRequest req, HttpServletResponse response) {

        // 返回 JWT 和其他信息
        return jwtAuthLoginService.adminAuthLogin(req,response);
    }

    //會員註冊
    @PostMapping("/register")
    @Operation(summary = "管理員註冊")
    public ResponseEntity<ApiResponseTemplate<String>> memberRegister(@RequestBody AdminRegisterRequest req) {

        // 返回 JWT 和其他信息
        return adminAuthService.adminRegister(req);
    }




}
