package com.exhibition.config;

import com.exhibition.exception.AuthenticationFailedException;
import com.exhibition.exception.AuthorizationFailedException;
import com.exhibition.mapper.UserAccountMapper;
import com.exhibition.utils.JwtUtil;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * JwtAuthenticationFilter
 * 驗證每個請求是否包含合法 JWT，若合法則將 userId 寫入 SecurityContext
 */

@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {


    @Value("${jwt.secret}")
    private String secretBase64;
    private SecretKey secretKey;

    public JwtAuthenticationFilter(UserAccountRepository userAccountRepository, UserRoleRepository userRoleRepository, JwtService jwtService, UserAccountMapper mapper) {
        this.userAccountRepository = userAccountRepository;
        this.userRoleRepository = userRoleRepository;
        this.jwtService = jwtService;
        this.mapper = mapper;
    }

    @PostConstruct
    public void init() {
        this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretBase64));
    }

    /**
     * 產生 Access Token
     *
     *
     */
    private final UserAccountRepository userAccountRepository;
    private final UserRoleRepository userRoleRepository;
    private final JwtService jwtService;
    private final UserAccountMapper mapper;


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");

        // 1. 確認是否有 Bearer token
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7); // 去除 "Bearer "
            // 2. 驗證 token 並解析 userId
            if (JwtUtil.validateToken(token, secretKey)) {
                String userEmail = JwtUtil.extractSubject(token, secretKey);
                // 3. 建立 authentication（可加入角色權限）
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userEmail, null, null);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        // **取得當前請求的 URI**
        String requestURI = request.getRequestURI();

        // **檢查是否是 Refresh Token 請求**
        boolean isRefreshRequest = requestURI.contains("/refreshToken");

        // **從 Cookie 或 Header 取得 Token**
        String token = getAuthToken(request, isRefreshRequest);

        if (token == null) {
            filterChain.doFilter(request, response);
            return;
        }

        if (!JwtUtil.validateToken(token, secretKey)) {
            log.warn("[Jwt] Token 驗證失敗");
            throw new AuthenticationFailedException("Token 驗證失敗");
        }
            // **檢查 Token 是否過期**
            if (JwtUtil.isTokenExpired(token, secretKey)) {
                clearAuthCookies(response);
               throw new AuthenticationFailedException("Token 已過期");

            }
        // 從 JWT 中取得資料
        String email = JwtUtil.extractSubject(token, secretKey);       // sub
        String userName = JwtUtil.extractUserName(token, secretKey);
        String userId = JwtUtil.extractUserId(token, secretKey);

        if (userName != null && email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                CustomUserDetail userDetails = loadUserByEmail(email);


                boolean isValidToken = isRefreshRequest ? jwtService.validateRefreshToken(token) : jwtService.validateToken(token);

                // **將用戶資訊存入 SecurityContext**
                SecurityContextHolder.getContext().setAuthentication(
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities())
                );
            }

        // 4. 放行
        filterChain.doFilter(request, response);
    }



    public CustomUserDetail loadUserByEmail(String email) throws UsernameNotFoundException {
        // 查詢用戶
        UserAccount userAccount = userAccountRepository.findByEmail(email)
                .orElseThrow(() ->  new AuthenticationFailedException("Email 未找到"));

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
        userData.setEmail(email);
        userData.setUserName(userAccount.getUsername());
        userData.setUserId(userAccount.getUserId());

        // 返回 Spring Security 的 UserDetails
        return new CustomUserDetail(userData, authorities);
    }


    private String getAuthToken(HttpServletRequest request, boolean isRefreshRequest) {
        String cookieName = isRefreshRequest ? "REFRESH_TOKEN" : "AUTH_TOKEN";

        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if (cookieName.equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }

        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }

        return null;
    }


    private void clearAuthCookies(HttpServletResponse response) {
        Cookie authCookie = new Cookie("AUTH_TOKEN", null);
        authCookie.setHttpOnly(true);
        authCookie.setSecure(true);
        authCookie.setPath("/");
        authCookie.setMaxAge(0);
        response.addCookie(authCookie);
    }
}
