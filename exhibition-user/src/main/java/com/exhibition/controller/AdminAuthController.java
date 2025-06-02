package com.exhibition.controller;


import com.exhibition.service.AdminAuthService;
import com.exhibition.service.JwtAuthLoginService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/sys/admin")
public class AdminAuthController {


    private final AdminAuthService adminAuthService;
    private final JwtAuthLoginService jwtAuthLoginService;


    public AdminAuthController(AdminAuthService adminAuthService, JwtAuthLoginService jwtAuthLoginService) {
        this.adminAuthService = adminAuthService;

        this.jwtAuthLoginService = jwtAuthLoginService;
    }





}
