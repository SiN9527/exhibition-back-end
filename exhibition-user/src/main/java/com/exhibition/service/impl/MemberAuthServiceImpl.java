package com.exhibition.service.impl;

import com.exhibition.dto.auth.MemberMainEntityDto;
import com.exhibition.dto.auth.MemberRegisterRequest;
import com.exhibition.dto.auth.VerificationRequest;
import com.exhibition.dto.user.*;
import com.exhibition.endpointService.NotificationService;
import com.exhibition.entity.member.MemberMainEntity;
import com.exhibition.entity.member.MemberMainRoleEntity;
import com.exhibition.entity.member.MemberMainRolePkEntity;
import com.exhibition.exception.ArgumentValidationException;
import com.exhibition.exception.AuthenticationFailedException;
import com.exhibition.exception.UnauthenticatedException;
import com.exhibition.mapper.MemberMainMapper;
import com.exhibition.repository.AdminMainRepository;
import com.exhibition.repository.MemberMainRepository;
import com.exhibition.repository.MemberMainRoleRepository;
import com.exhibition.service.JwtMemberDetailsService;
import com.exhibition.service.JwtService;
import com.exhibition.service.MemberAuthService;
import com.exhibition.utils.CodeGeneratorUtils;
import com.exhibition.utils.JwtUtil;
import com.exhibition.utils.MapperUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import javax.crypto.SecretKey;
import java.security.SecureRandom;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import static com.exhibition.enums.TokenPurpose.*;

@Slf4j
@Service
@AllArgsConstructor
public class MemberAuthServiceImpl implements MemberAuthService {

    private final MemberMainRepository memberMainRepository;
    // 使用 LoggerFactory 建立 Logger 實例，傳入當前類別作為參數
    private static final Logger logger = LoggerFactory.getLogger(MemberAuthServiceImpl.class);
    private final JwtUtil jwtUtil;
    private final JwtService jwtService;
    private final JwtMemberDetailsService memberDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final CodeGeneratorUtils codeGeneratorUtils;

    private final MemberMainMapper memberMainMapper;

    private final NotificationService notificationService;
    private final AdminMainRepository userRepository;

    private final MemberMainRoleRepository memberMainRoleRepository;
    private final MapperUtils mapperUtils;



    /**
     * 註冊 API，建立新使用者後回傳統一格式的成功訊息。
     *
     * @param req 前端傳入的使用者註冊資料
     * @return 統一格式的 ApiResponse 物件，payload 為成功訊息
     */
    @Transactional
    @Override
    public String memberRegister(@RequestBody MemberRegisterRequest req) {


        // 驗證 email 是否已存在
        if (memberMainRepository.existsByEmail(req.getEmail())) {
            throw new AuthenticationFailedException("Email already exists");
        }

        // 密碼格式驗證
        if (isValidPassword(req.getPassword())) {
            throw new ArgumentValidationException(
                    List.of("password"),
                    List.of("Password must contain upper/lower case, number, min length 8.")
            );
        }

        Timestamp timeNow = new Timestamp(System.currentTimeMillis());
        // 建立新使用者實體，並設定相關欄位
        MemberMainEntity entity = MapperUtils.map(req, MemberMainEntity.class);
        String id = codeGeneratorUtils.generateRegNo(req.getEmail());
        entity.setMemberId(id); // 產生隨機的 memberId
        entity.setPassword(passwordEncoder.encode(req.getPassword()));
        entity.setEnabled(false); // 預設帳號未啟用
        entity.setCreateTime(timeNow); // 設定建立時間
        entity.setRegistrationDate(timeNow); // 設定註冊時間
        entity.setEventId("Event001");
        memberMainRepository.save(entity);


        MemberMainRolePkEntity pk = new MemberMainRolePkEntity(); // 建立主鍵
        pk.setMemberId(id); // uuid
        pk.setRoleId(2L); // role 的角色表

        MemberMainRoleEntity memberMainRole = new MemberMainRoleEntity();
        memberMainRole.setPk(pk);
        memberMainRole.setCreatedBy("SYS"); // 系統自動輸入
        memberMainRoleRepository.save(memberMainRole);
        String token = jwtService.generateVerificationToken(id,req.getEmail(), ACCOUNT_REGISTRATION.getValue());
        notificationService.sendRegistrationVerifyMail(
                memberMainMapper.MemberMainEntityToMemberMainEntityDto(entity),token,ACCOUNT_REGISTRATION.getValue());

        return  "User registered successfully ! Please check your email to verify your account.";
    }

    @Override
    @Transactional
    public String verifyEmail(@RequestBody VerificationRequest req, HttpServletResponse response) {

        String token = req.getToken();

        SecretKey secretKey = jwtUtil.getSecretKey();

        if (jwtUtil.isTokenExpired(token)) {

            throw new UnauthenticatedException();
        }
        String email = jwtUtil.extractSubject(token);
        String id = jwtUtil.extractUserId(token);
        String purpose = jwtUtil.extractPurpose(token);
        // **更新資料庫，標記使用者已驗證**
        MemberMainEntity member = memberMainRepository.findByEmail(email)
                .orElse(null);
        if (member == null) {
           throw new AuthenticationFailedException("Member not found with email: " + email);
        }
        switch (fromPurpose(purpose)) {
            case ACCOUNT_REGISTRATION -> {
                member.setEnabled(true);
                memberMainRepository.save(member);
                return "Account verification successful, please log in";
            }
            case EMAIL_VERIFICATION -> {
                String oldEmail = member.getEmail();
                member.setEmail(member.getEmailTemp());
                member.setEmailTemp(oldEmail);
                memberMainRepository.save(member);
                return "Email verification successful, please log in again";
            }
            case PASSWORD_RESET -> {
                member.setNeedResetPwd(false);
                memberMainRepository.save(member);
                return "Password reset successful, please log in again";
            }
            default -> {


                throw  new AuthenticationFailedException("Unknown verification purpose: " + purpose);
            }
        }
    }



    // **忘記密碼 API**
    @Override
    @Transactional
    public String memberForgotPwd(MemberPwdUpdateRequest req) {
        String email = req.getEmail();
        Optional<MemberMainEntity> memberOpt = memberMainRepository.findByEmail(email);
        if (memberOpt.isEmpty()) {
              throw new AuthenticationFailedException("Member not found with email: " + email);
                 }
        MemberMainEntity member = memberOpt.get();
        MemberMainEntityDto memberMainEntityDto = memberMainMapper.MemberMainEntityToMemberMainEntityDto(member);

        String tempPassword = generateTempPassword();
        member.setPassword(passwordEncoder.encode(tempPassword));
        member.setNeedResetPwd(true);
        memberMainRepository.save(member);

        String token = jwtService.generateVerificationToken(member.getMemberId(),email, PASSWORD_RESET.getValue());
        notificationService.sendTempPasswordEmail(memberMainEntityDto, tempPassword, token, PASSWORD_RESET.getValue());
        return "Temporary password sent to your email.";
    }

    @Override
    @Transactional
    public String memberResetPasswordAfterLogin(MemberResetPwdRequest req, UserDetails userDetails, HttpServletResponse response) {


        String id = userDetails.getUsername();//memberId
        Optional<MemberMainEntity> memberOpt = memberMainRepository.findByMemberId(id);
        if (memberOpt.isEmpty()) {
            throw new AuthenticationFailedException("Member not found with ID: " + id);
        }
        MemberMainEntity member = memberOpt.get();
        MemberMainEntityDto memberMainEntityDto = memberMainMapper.MemberMainEntityToMemberMainEntityDto(member);

        // 驗證舊密碼是否正確
        if (!passwordEncoder.matches(req.getPassword(), member.getPassword())) {
            throw new ArgumentValidationException(
                    List.of("origPassword"),
                    List.of("Incorrect original password.")
            );
        }

        // 設定新密碼與移除重設旗標
        member.setPassword(passwordEncoder.encode(req.getNewPassword()));
        member.setNeedResetPwd(false);
        memberMainRepository.save(member);

        String token = jwtService.generateVerificationToken(member.getMemberId(),member.getEmail(), PASSWORD_SUCCESS.getValue());
        notificationService.sendResetPwdSuccessEmail(memberMainEntityDto, token, PASSWORD_SUCCESS.getValue());

        // 清除 Cookie
        clearCookies(response);


        return"Password updated successfully. Please log in again.";
    }






    /**
     * 會員更改 Email 並發送新驗證信
     */
    @Override
    @Transactional
    public String memberUpdateEmail(MemberEmailUpdateRequest req, UserDetails userDetails) {

        Optional<MemberMainEntity> memberOpt = memberMainRepository.findByMemberId(userDetails.getUsername());
        if (memberOpt.isEmpty()) {
            throw new AuthenticationFailedException("Member not found with ID: " + userDetails.getUsername());
        }
        MemberMainEntity member = memberOpt.get();
        MemberMainEntityDto memberMainEntityDto = memberMainMapper.MemberMainEntityToMemberMainEntityDto(member);


        // 更新 Email
        String newEmail = req.getNewEmail();
        if (memberMainRepository.existsByEmail(newEmail)) {
            throw new AuthenticationFailedException("Email already exists");
        }
        member.setEmailTemp(newEmail); // 暫存新 Email
        memberMainRepository.save(member);

        //發送驗證信
        String token = jwtService.generateVerificationToken(member.getMemberId(),req.getNewEmail(), EMAIL_VERIFICATION.getValue());
        //send verification email
        notificationService.sendMailVerificationEmail(req.getNewEmail(), token, EMAIL_VERIFICATION.getValue());

        return "Email updated. Please check your new email to verify your account.";
    }


    // 密碼格式驗證
    private boolean isValidPassword(String password) {
        // 密碼必須包含大寫字母、小寫字母、數字，且長度至少 8 位
        return !password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[A-Za-z\\d]{8,}$");
    }





    /**
     * 會員登出 API
     */
    @Override
    public String memberLogout(HttpServletResponse response) {
        // **清除 Cookie**
        // **清除 Cookie**
        Cookie accessCookie = new Cookie("AUTH_TOKEN", null);
        clearCookies(accessCookie);

        Cookie refreshCookie = new Cookie("REFRESH_TOKEN", null);
        clearCookies(refreshCookie);
        SecurityContextHolder.clearContext();
        response.addCookie(accessCookie);
        response.addCookie(refreshCookie);
        return "Logout successful";
    }


    private void clearCookies(Cookie cookie) {
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);
    }

    /** 清除認證 Cookie */
    private void clearCookies(HttpServletResponse response) {
        Cookie accessCookie = new Cookie("AUTH_TOKEN", null);
        accessCookie.setHttpOnly(true);
        accessCookie.setSecure(true);
        accessCookie.setPath("/");
        accessCookie.setMaxAge(0);

        Cookie refreshCookie = new Cookie("REFRESH_TOKEN", null);
        refreshCookie.setHttpOnly(true);
        refreshCookie.setSecure(true);
        refreshCookie.setPath("/");
        refreshCookie.setMaxAge(0);

        response.addCookie(accessCookie);
        response.addCookie(refreshCookie);
    }

    private String generateTempPassword() {
        int length = 10;
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$";
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }

}


// // **使用 Spring Security 的 `authenticate()` 驗證身份**  不手動loadUser
//    Authentication authentication = authenticationManager.authenticate(
//            new UsernamePasswordAuthenticationToken(email, password)
//    );
//
//    log.info("驗證通過: {}", authentication.getName());
//
//    // **取得 UserDetails**
//    UserDetails userDetails = (UserDetails) authentication.getPrincipal();