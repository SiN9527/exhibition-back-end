package com.exhibition.controller;


import com.exhibition.dto.auth.MemberLoginRequest;
import com.exhibition.dto.auth.MemberRegisterRequest;
import com.exhibition.dto.auth.VerificationRequest;
import com.exhibition.dto.user.MemberEmailUpdateRequest;
import com.exhibition.dto.user.MemberPwdUpdateRequest;
import com.exhibition.dto.user.MemberResetPwdRequest;
import com.exhibition.exception.handler.AuthUsecaseExcHandler;
import com.exhibition.exception.payload.ProcessPayload;
import com.exhibition.service.JwtAuthLoginService;
import com.exhibition.service.MemberAuthService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/member/api")
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
    @PostMapping("/sys/login")
    public ProcessPayload<String> login(@RequestBody MemberLoginRequest request,
                                        HttpServletResponse response) {
        return AuthUsecaseExcHandler.runFor(() -> jwtAuthLoginService.memberAuthLogin(request, response))
                .execute()
                .setSnackbarIfTemporary("登入失敗:暫時無法登入、請稍後嘗試")
                .setSnackbarIfArgInvalide("登入失敗:輸入欄位驗證錯誤")
                .setSnackbarIfPrevent("登入失敗:使用者名稱或密碼錯誤")
                .throwIfServiceException();
    }

    /**
     * 使用者註冊
     */
    @Operation(summary = "使用者註冊")
    @PostMapping("//sys/register")
    public ProcessPayload<String> register(@RequestBody MemberRegisterRequest request,
                                           HttpServletResponse response) {
        return AuthUsecaseExcHandler.runFor(() -> memberAuthService.memberRegister(request))
                .execute()
                .setSnackbarIfTemporary("註冊失敗:暫時無法註冊、請稍後嘗試")
                .setSnackbarIfArgInvalide("註冊失敗:輸入欄位驗證錯誤")
                .setSnackbarIfPrevent("註冊失敗:使用者名稱或信箱已存在")
                .throwIfServiceException();
    }


    @Operation(summary = "信箱點擊網址驗證")
    @PostMapping("/sys/verify")
    public ProcessPayload<String> verify(@RequestBody VerificationRequest request, HttpServletResponse response) {
        return AuthUsecaseExcHandler.runFor(() -> memberAuthService.verifyEmail(request, response))
                .execute()
                .setSnackbarIfTemporary("驗證失敗:暫時無法驗證、請稍後嘗試")
                .setSnackbarIfArgInvalide("信箱驗證失敗:輸入欄位驗證錯誤")
                .setSnackbarIfPrevent("驗證失敗:使用者名稱或密碼錯誤")
                .throwIfServiceException();
    }

    /**
     * 忘記密碼：發送臨時密碼
     * @param req ForgotPwdReq DTO，包含 email
     * @return ProcessPayload<String> 處理結果
     */
    @PostMapping("/sys/forgotPwd")
    public ProcessPayload<String> userForgotPwd(@RequestBody MemberPwdUpdateRequest req) {
        return AuthUsecaseExcHandler.runFor(() -> memberAuthService.memberForgotPwd(req))
                .execute()
                .setSnackbarIfTemporary("忘記密碼失敗:暫時無法發送臨時密碼、請稍後嘗試")
                .setSnackbarIfArgInvalide("忘記密碼失敗:輸入欄位驗證錯誤")
                .setSnackbarIfPrevent("忘記密碼失敗:使用者名稱或信箱錯誤")
                .throwIfServiceException();
    }


    @PreAuthorize("isAuthenticated()")
    @PostMapping("/auth/resetPwd")
    public ProcessPayload<String> userResetPwd(@RequestBody MemberResetPwdRequest req,
                                               @AuthenticationPrincipal UserDetails userDetails,
                                               HttpServletResponse response) {
        return AuthUsecaseExcHandler.runFor(() -> memberAuthService.memberResetPasswordAfterLogin(req, userDetails, response))
                .execute()
                .setSnackbarIfArgInvalide("重設密碼失敗:輸入欄位驗證錯誤")
                .setSnackbarIfTemporary("重設密碼失敗:暫時無法重設密碼、請稍後嘗試")
                .setSnackbarIfPrevent("重設密碼失敗:使用者名稱或密碼錯誤")
                .throwIfServiceException();
    }

    /**
     * 使用者登出
     */
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "使用者登出")
    @PostMapping("/auth/logout")
    public ProcessPayload<String> logout(HttpServletResponse response) {

        return AuthUsecaseExcHandler.runFor(() -> memberAuthService.memberLogout(response))
                .execute()
                .setSnackbarIfTemporary("登出失敗:暫時無法登出、請稍後嘗試")
                .setSnackbarIfPrevent("登出失敗:使用者名稱或密碼錯誤")
                .throwIfServiceException();
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/auth/updateEmail")
    public ProcessPayload<String> userUpdateEmail(@RequestBody MemberEmailUpdateRequest req,
                                                  @AuthenticationPrincipal UserDetails userDetails) {
        return AuthUsecaseExcHandler.runFor(() -> memberAuthService.memberUpdateEmail(req, userDetails))
                .execute()
                .setSnackbarIfArgInvalide("更新 Email 失敗:輸入欄位驗證錯誤")
                .setSnackbarIfTemporary("更新 Email 失敗:暫時無法更新、請稍後嘗試")
                .setSnackbarIfPrevent("更新 Email 失敗:使用者名稱或密碼錯誤")
                .throwIfServiceException();
    }


}
