package com.exhibition.controller;


import com.exhibition.dto.ApiResponseTemplate;
import com.exhibition.dto.auth.AdminLoginRequest;
import com.exhibition.dto.auth.AdminRegisterRequest;
import com.exhibition.dto.auth.MemberLoginRequest;
import com.exhibition.dto.auth.MemberRegisterRequest;
import com.exhibition.exception.handler.AuthUsecaseExcHandler;
import com.exhibition.exception.payload.ProcessPayload;
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
@RequestMapping("/api/admin")
public class AdminSysController {


    private final AdminAuthService adminAuthService;
    private final JwtAuthLoginService jwtAuthLoginService;

    public AdminSysController(AdminAuthService adminAuthService, JwtAuthLoginService jwtAuthLoginService) {
        this.adminAuthService = adminAuthService;

        this.jwtAuthLoginService = jwtAuthLoginService;
    }

    @PostMapping("/sys/login")
    @Operation(summary = "管理員登入")
    public ProcessPayload<String> login(@RequestBody AdminLoginRequest request,
                                        HttpServletResponse response) {
        return AuthUsecaseExcHandler.runFor(() -> jwtAuthLoginService.adminAuthLogin(request, response))
                .execute()
                .setSnackbarIfTemporary("登入失敗:暫時無法登入、請稍後嘗試")
                .setSnackbarIfArgInvalide("登入失敗:輸入欄位驗證錯誤")
                .setSnackbarIfPrevent("登入失敗:使用者名稱或密碼錯誤")
                .throwIfServiceException();
    }

    //會員註冊
    @PostMapping("/sys/register")
    @Operation(summary = "管理員註冊")
    public ProcessPayload<String> register(@RequestBody AdminRegisterRequest request,
                                           HttpServletResponse response) {
        return AuthUsecaseExcHandler.runFor(() -> adminAuthService.adminRegister(request))
                .execute()
                .setSnackbarIfTemporary("註冊失敗:暫時無法註冊、請稍後嘗試")
                .setSnackbarIfArgInvalide("註冊失敗:輸入欄位驗證錯誤")
                .setSnackbarIfPrevent("註冊失敗:使用者名稱或信箱已存在")
                .throwIfServiceException();
    }







}
