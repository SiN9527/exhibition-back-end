package com.exhibition.controller;

import com.exhibition.dto.user.MemberProfileResponse;
import com.exhibition.dto.user.MemberPwdUpdateRequest;
import com.exhibition.dto.user.MemberResetPwdRequest;
import com.exhibition.dto.user.MemberUpdateRequest;
import com.exhibition.service.MemberAuthService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth/member")
public class MemberAuthController {


    private final MemberAuthService memberAuthService;


    public MemberAuthController(MemberAuthService memberAuthService) {
        this.memberAuthService = memberAuthService;

    }

    //會員登出
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/logout")
    @Operation(summary = "會員登出")
    public ResponseEntity<ApiResponseTemplate<?>> memberLogout(HttpServletResponse response) {
        return memberAuthService.memberLogout(response);
    }

    //{ withCredentials: true }

    //取得個人資料
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/profile")
    @Operation(summary = "登入者取得個人資料")
    public ResponseEntity<ApiResponseTemplate<MemberProfileResponse>> memberGetProfile(@AuthenticationPrincipal UserDetails userDetails) {
        return memberAuthService.memberGetProfile(userDetails);
    }

    //會員重設密碼
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/resetPwd")
    @Operation(summary = "會員重設密碼")
    public ResponseEntity<ApiResponseTemplate<?>> memberResetPwd(@RequestBody MemberResetPwdRequest req, @AuthenticationPrincipal UserDetails userDetails, HttpServletResponse response) {
        return memberAuthService.memberResetPasswordAfterLogin(req, userDetails,response);
    }

    // 修改密碼
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/updatePwd")
    @Operation(summary = "登入者修改密碼")
    public ResponseEntity<ApiResponseTemplate<?>> memberUpdatePwd(@RequestBody MemberPwdUpdateRequest req, @AuthenticationPrincipal UserDetails userDetails, HttpServletResponse response) {
        return memberAuthService.memberUpdatePwd(req, userDetails, response);
    }

    // 修改密碼
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/updateProfile")
    @Operation(summary = "登入者修改資料")
    public ResponseEntity<ApiResponseTemplate<?>> memberUpdatePwd(@RequestBody MemberUpdateRequest req, @AuthenticationPrincipal UserDetails userDetails, HttpServletResponse response) {
        return memberAuthService.memberUpdateProfile(req, userDetails, response);
    }

    //更新token
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/refreshToken")
    @Operation(summary = "登入者 刷新 Cookie 與Token ")
    public ResponseEntity<ApiResponseTemplate<?>> memberRefreshToken(@CookieValue(value = "REFRESH_TOKEN", required = false) String refreshToken,
                                                                     HttpServletResponse response) {
        return memberAuthService.memberRefreshToken(refreshToken, response);
    }

    ;

}
