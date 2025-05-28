package com.exhibition.service;

import com.exhibition.dto.auth.AdminLoginRequest;
import com.exhibition.dto.auth.MemberRegisterRequest;
import com.exhibition.dto.auth.VerificationRequest;
import com.exhibition.dto.user.*;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

public interface MemberService {




    // cookie取得資料
    public MemberProfileResponse memberGetProfile(UserDetails userDetails);



    // 更新個人資料
    public String memberUpdateProfile(MemberUpdateRequest req, UserDetails userDetails, HttpServletResponse response);




}
