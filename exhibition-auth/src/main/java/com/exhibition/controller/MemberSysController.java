package com.exhibition.controller;


import com.exhibition.dto.ApiResponseTemplate;
import com.exhibition.dto.auth.LoginRequest;
import com.exhibition.dto.auth.MemberLoginRequest;
import com.exhibition.dto.auth.MemberRegisterRequest;
import com.exhibition.dto.user.MemberPwdUpdateRequest;
import com.exhibition.exception.handler.AuthUsecaseExcHandler;
import com.exhibition.exception.payload.ProcessPayload;
import com.exhibition.service.JwtAuthLoginService;
import com.exhibition.service.MemberAuthService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/sys/member")
public class MemberSysController {


    private final MemberAuthService memberAuthService;
    private final JwtAuthLoginService jwtAuthLoginService;

    public MemberSysController(MemberAuthService memberAuthService, JwtAuthLoginService jwtAuthLoginService) {
        this.memberAuthService = memberAuthService;

        this.jwtAuthLoginService = jwtAuthLoginService;
    }



    /**
     * 使用者登入
     */
    @Operation(summary = "使用者登入")
    @PostMapping("/login")
    public ProcessPayload<String> login(@RequestBody MemberLoginRequest request,
                                        HttpServletResponse response) {
        return AuthUsecaseExcHandler.runFor(() -> jwtAuthLoginService.memberAuthLogin(request, response))
                .execute()
                .setSnackbarIfTemporary("登入失敗:暫時無法登入、請稍後嘗試")
                .setSnackbarIfArgInvalide("登入失敗:輸入欄位驗證錯誤")
                .setSnackbarIfPrevent("登入失敗:使用者名稱或密碼錯誤")
                .throwIfServiceException();
    }

    //會員註冊
    @PostMapping("/register")
    @Operation(summary = "會員註冊")
    public ResponseEntity<ApiResponseTemplate<String>> memberRegister(@RequestBody MemberRegisterRequest req) {

        // 返回 JWT 和其他信息
        return memberAuthService.memberRegister(req);
    }


    //會員驗證
    @PostMapping("/verify")
    @Operation(summary = "會員註冊驗證")
    public ResponseEntity<ApiResponseTemplate<String>> verifyEmail(@RequestBody Map<String, String> token, HttpServletResponse response) {
        return memberAuthService.verifyEmail(token, response);
    }

    //會員忘記密碼
    @PostMapping("/forgotPwd")
    @Operation(summary = "會員找回密碼")
    public ResponseEntity<ApiResponseTemplate<?>> memberForgotPwd(@RequestBody MemberPwdUpdateRequest req, @AuthenticationPrincipal UserDetails userDetails) {
        return memberAuthService.memberForgotPwd(req);
    }





}
