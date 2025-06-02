package com.exhibition.controller;


import com.exhibition.dto.user.MemberProfileResponse;
import com.exhibition.dto.user.MemberUpdateRequest;
import com.exhibition.exception.handle.UsecaseExcHandler;
import com.exhibition.exception.payload.ProcessPayload;
import com.exhibition.service.MemberService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/member/auth")
public class MemberController {


    private final MemberService memberService;


    public MemberController(MemberService memberService) {
        this.memberService = memberService;

    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/profile")
    public ProcessPayload<MemberProfileResponse> userDataQuery(@AuthenticationPrincipal UserDetails userDetails) {

        return UsecaseExcHandler.runFor(() -> memberService.memberGetProfile(userDetails)).execute().setSnackbarIfTemporary("更新失敗:暫時無法更新、請稍後嘗試")
                .setSnackbarIfPrevent("無法取得用戶資料").throwIfServiceException();

    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/updateProfile")
    public ProcessPayload<String> updateUserProfile(@AuthenticationPrincipal UserDetails userDetails, @RequestBody MemberUpdateRequest req, HttpServletResponse response) {
        return UsecaseExcHandler.runFor(() -> memberService.memberUpdateProfile(req, userDetails, response)).execute()
                .setSnackbarIfTemporary("更新失敗:暫時無法更新、請稍後嘗試")
                .setSnackbarIfArgInvalide("更新失敗:輸入欄位驗證錯誤").throwIfServiceException();
    }

}
