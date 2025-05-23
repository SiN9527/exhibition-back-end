package com.exhibition.service.impl;

import com.alibaba.fastjson.JSON;
import com.exhibition.endpointService.NotificationService;
import com.exhibition.endpointService.UserService;
import com.exhibition.exception.*;
import com.exhibition.mapper.UserAccountMapper;
import com.exhibition.utils.CodeGeneratorUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import javax.crypto.SecretKey;
import java.security.SecureRandom;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.exhibition.enums.TokenPurpose.*;


@AllArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {
    // 使用 LoggerFactory 建立 Logger 實例，傳入當前類別作為參數
    private static final Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);

    private final PasswordEncoder passwordEncoder;
    private final UserAccountRepository userAccountRepository;
    private final UserRoleRepository userRoleRepository;

    @Qualifier("userAccountMapper")
    private final UserAccountMapper mapper;

    private final CodeGeneratorUtils codeGeneratorUtils;
    private final UserService userService;

    private final NotificationService notificationService;

    private final JwtService jwtService;
    private final RoleRepository roleRepository;


    /**
     * user register
     *
     * @param req      RegiReq
     * @param response HttpServletResponse
     * @return ResponseEntity<String>
     */
    @Transactional
    @Override
    public ProcessPayload<String> userAuthRegi(RegiReq req, HttpServletResponse response) {

        String email = req.getEmail();
        String password = req.getPassword();
        UserAccount userAccount = new UserAccount();

        if (userExists(email)) {
            // 使用 ApiResponse.fail() 包裝失敗訊息，再回傳 ResponseEntity
            throw new AuthenticationFailedException("Email 已存在");
        }

        // 密碼格式驗證
        if (isValidPassword(req.getPassword())) {
            throw new ArgumentValidationException(
                    List.of("password"),
                    List.of("密碼需至少 8 碼，含大小寫字母與數字")
            );
        }
        String userId = codeGeneratorUtils.generateRegNo(email);
        userAccount.setUserId(userId);
        userAccount.setEmail(email);
        userAccount.setPassword(passwordEncoder.encode(password)); // **加密密碼**
        userAccount.setUsername(req.getUserName());
        userAccount.setPhone(req.getPhone());
        userAccount.setEnabled(false);
        userAccountRepository.save(userAccount); // 儲存用戶資料


        UserRole userRole = new UserRole();
        userRole.setUser(userAccount);
        Role role = roleRepository.findById(5L)
                .orElseThrow(() -> new AuthenticationFailedException("指定的角色不存在"));
        userRole.setRole(role);
        userRole.setUserId(userId);
        userRole.setRoleId(role.getId());

        userRoleRepository.save(userRole);


        UserAccountDto userAccountDto = mapper.UserAccountToUserAccountDto(userAccount);
        //讓USER模組處理profile等事情
        req.setId(userId);
        RegiResp regiResp = userService.userRegister(req);

        String token = jwtService.generateVerificationToken(userId,email, ACCOUNT_REGISTRATION.getValue());
        notificationService.sendRegistrationVerifyMail(userAccountDto, token, ACCOUNT_REGISTRATION.getValue());

        // 這裡可以使用 JavaMailSender 發送驗證信件
        // 例如：mailSender.send(verificationEmail);
        return ProcessPayload.success("註冊成功，請至信箱驗證");

    }


    /**
     * user login
     *
     * @param req
     * @param response
     * @return
     */
    @Override
    public ProcessPayload<String> userAuthLogin(LoginReq req, HttpServletResponse response) {

        String email = req.getEmail();
        String password = req.getPassword();

        logger.info("login email: {}", email);
        // 確定身份類型（USER 或 MEMBER）

        Optional<UserAccount> userAccount = userAccountRepository.findByEmail(email);
        if (userAccount.isEmpty()) {
            throw new AuthenticationFailedException("INVALID_EMAIL_OR_PASSWORD");
        }
        UserAccount userData = userAccount.get();

        String storedEncryptedPassword;


        CustomUserDetail customUserDetail = loadUserByEmail(userData);
        storedEncryptedPassword = userData.getPassword(); // **取出加密後的密碼**

        // **比對密碼解密**
        if (!passwordEncoder.matches(password, storedEncryptedPassword)) {
            throw new ArgumentValidationException(
                    List.of("password"),
                    List.of("密碼錯誤")
            );
        }
        ;
        // 取得登入 IP 與 User-Agent


        // 生成 JWT
        generateNewJwtToken(customUserDetail, response);
        // 回應成功消息
        return ProcessPayload.success("登入成功");

    }


    /**
     * user forgot password
     *
     * @param req
     * @return
     */
    @Override
    @Transactional
    public ProcessPayload<String> userForgotPwd(ForgotPwdReq req) {
        String email = req.getEmail();
        Optional<UserAccount> userAccount = userAccountRepository.findByEmail(email);
        if (userAccount.isEmpty()) {
            throw new AuthenticationFailedException("帳號不存在");
        }
        UserAccountDto userAccountDto = userAccount.map(mapper::UserAccountToUserAccountDto).orElse(null);

        String tempPassword = generateTempPassword();
        userAccount.get().setPassword(passwordEncoder.encode(tempPassword));
        userAccount.get().setNeedResetPwd(true);
        userAccountRepository.save(userAccount.get());

        String token = jwtService.generateVerificationToken(userAccount.get().getUserId(),email, PASSWORD_RESET.getValue());
        notificationService.sendTempPasswordEmail(userAccountDto, tempPassword, token, PASSWORD_RESET.getValue());
        return ProcessPayload.success("臨時密碼已寄出");
    }

    /**
     * user reset password
     *
     * @param req
     * @param userDetails
     * @param response
     * @return
     */
    @Override
    @Transactional
    public ProcessPayload<String> userResetPwd(EditPwdReq req, UserDetails userDetails, HttpServletResponse response) {

        String username = userDetails.getUsername();
        Optional<UserAccount> userAccount = userAccountRepository.findByUsername(username);
        if (userAccount.isEmpty()) {
            throw new AuthenticationFailedException("帳號不存在");
        }
        UserAccount userAccountEntity = userAccount.get();
        UserAccountDto userAccountDto = userAccount.map(mapper::UserAccountToUserAccountDto).orElse(null);
        // 驗證舊密碼是否正確
        if (!passwordEncoder.matches(req.getOrigPassword(), userAccountEntity.getPassword())) {
           throw new ArgumentValidationException(
                    List.of("origPassword"),
                    List.of("舊密碼不正確")
            );
        }

        // 設定新密碼與移除重設旗標
        userAccountEntity.setPassword(passwordEncoder.encode(req.getNewPassword()));
        userAccountEntity.setNeedResetPwd(false);
        userAccountRepository.save(userAccountEntity);

        String token = jwtService.generateVerificationToken(userAccountEntity.getUserId(),userAccountEntity.getEmail(), PASSWORD_SUCCESS.getValue());
        notificationService.sendResetPwdSuccessEmail(userAccountDto, token, PASSWORD_SUCCESS.getValue());

        // 清除 Cookie
        clearCookies(response);

        return ProcessPayload.success("密碼更新成功，請重新登入");
    }


    /**
     * user update email
     *
     * @param req
     * @param userDetails
     * @return
     */
    @Override
    @Transactional
    public ProcessPayload<String> userUpdateEmail(EditEmailReq req, UserDetails userDetails) {

        Optional<UserAccount> userAccount = userAccountRepository.findByUsername(userDetails.getUsername());
        if (userAccount.isEmpty()) {
            throw new AuthenticationFailedException("帳號不存在");
        }
        UserAccount userAccountEntity = userAccount.get();
        userAccountEntity.setEmailTemp(req.getNewEmail());
        userAccountRepository.save(userAccountEntity);
        String token = jwtService.generateVerificationToken(userAccountEntity.getUserId(),req.getNewEmail(), EMAIL_VERIFICATION.getValue());
        //send verification email
        notificationService.sendMailVerificationEmail(req.getNewEmail(), token, EMAIL_VERIFICATION.getValue());

      return ProcessPayload.success("驗證信已寄出，請至信箱驗證");
    }


    /**
     * verify email token
     *
     * @param req
     * @param response
     * @return
     */
    @Override
    @Transactional
    public ProcessPayload<String> verifyMail(@RequestBody VerificationRequest req, HttpServletResponse response) {
        String token = req.getToken();

        SecretKey secretKey = jwtService.getSecretKey();

        if (JwtUtil.isTokenExpired(token, secretKey)) {

                  throw new UnauthenticatedException();
        }

        String email = JwtUtil.extractSubject(token, secretKey);
        String userId = JwtUtil.extractUserId(token, secretKey);
        String purpose = JwtUtil.extractPurpose(token, secretKey);
        logger.info( "verifyMai2222222222222l: {}", JSON.toJSONString(userId));
        Optional<UserAccount> userAccount = userAccountRepository.findById(userId);
        if (userAccount.isEmpty()) {
           throw new AuthenticationFailedException("帳號不存在");
        }

        UserAccount account = userAccount.get();

        switch (fromPurpose(purpose)) {
            case ACCOUNT_REGISTRATION -> {
                account.setEnabled(true);
                userAccountRepository.save(account);
                return ProcessPayload.success("帳號啟用成功，請重新登入");
            }
            case EMAIL_VERIFICATION -> {
                String oldEmail = account.getEmail();
                account.setEmail(account.getEmailTemp());
                account.setEmailTemp(oldEmail);
                userAccountRepository.save(account);
                return ProcessPayload.success("Email 更新成功，請重新登入");
            }
            case PASSWORD_RESET -> {
                account.setNeedResetPwd(false);
                userAccountRepository.save(account);
                return ProcessPayload.success("密碼重設成功，請重新登入");
            }
            default -> {


                       throw  new AuthenticationFailedException("無效的 Token 或 未知錯誤");
            }
        }
    }


    /**
     * 產生新的 JWT Token
     *
     * @param customUserDetail
     * @param response
     */
    private void generateNewJwtToken(CustomUserDetail customUserDetail, HttpServletResponse response) {
        List<String> roles = customUserDetail.getAuthorities().stream()
                .map(auth -> auth.getAuthority().replace("ROLE_", ""))
                .toList();
        String accessToken = jwtService.generateAccessToken(customUserDetail.getEmail(),customUserDetail.getUserId(), customUserDetail.getUsername(), roles);
        String refreshToken = jwtService.generateRefreshToken(customUserDetail.getEmail());

        // **存入 HttpOnly Cookie**
        Cookie accessCookie = new Cookie("AUTH_TOKEN", accessToken);
        accessCookie.setHttpOnly(true);
        accessCookie.setSecure(true);
        accessCookie.setPath("/");
        accessCookie.setMaxAge(60 * 60 * 2); // 2 小時有效

        Cookie refreshCookie = new Cookie("REFRESH_TOKEN", refreshToken);
        refreshCookie.setHttpOnly(true);
        refreshCookie.setSecure(true);
        refreshCookie.setPath("/");
        refreshCookie.setMaxAge(7 * 24 * 60 * 60);  // 7 天有效

        response.addCookie(accessCookie);
        response.addCookie(refreshCookie);
    }


    /**
     * 透過 email 查詢用戶
     *
     * @param
     * @return CustomUserDetail
     * @throws UsernameNotFoundException
     */
    @Override
    public CustomUserDetail loadUserByEmail(UserAccount userAccount) throws UsernameNotFoundException {
        // 查詢用戶

        if (!userAccount.getEnabled()) {
            throw new AuthorizationFailedException("帳號尚未啟用");
        }

        // 透過關聯表查詢該用戶的角色
        List<Role> roles = userRoleRepository.findRolesByUserId(userAccount.getUserId());

        // 轉換成 Spring Security 需要的角色格式
        List<GrantedAuthority> authorities = roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getRoleCode())) // Spring Security 需要加 "ROLE_"
                .collect(Collectors.toList());
        JwtUserDataDto userData = new JwtUserDataDto();
        userData.setEmail(userAccount.getEmail());
        userData.setUserName(userAccount.getUsername());
        userData.setUserId(userAccount.getUserId());

        logger.info( "loadUserByEmail: {}", JSON.toJSONString(userData));

        // 返回 Spring Security 的 UserDetails
        return new CustomUserDetail(userData, authorities);
    }


    /**
     * 會員登出 API
     */
    @Override
    public ProcessPayload<String> userLogout(HttpServletResponse response) {

        Cookie accessCookie = new Cookie("AUTH_TOKEN", null);
        clearCookies(accessCookie);

        Cookie refreshCookie = new Cookie("REFRESH_TOKEN", null);
        clearCookies(refreshCookie);
        SecurityContextHolder.clearContext();
        response.addCookie(accessCookie);
        response.addCookie(refreshCookie);
        return ProcessPayload.success("登出成功");
    }


    public boolean userExists(String email) {
        return userAccountRepository.existsByEmail(email);
    }


    //temp pwd maker
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

    private boolean isValidPassword(String password) {
        // 密碼必須包含大寫字母、小寫字母、數字，且長度至少 8 位
        return !password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[A-Za-z\\d]{8,}$");
    }

    //clear cookies
    private void clearCookies(Cookie cookie) {
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);
    }

    /**
     * clear all Cookie
     */
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

}

