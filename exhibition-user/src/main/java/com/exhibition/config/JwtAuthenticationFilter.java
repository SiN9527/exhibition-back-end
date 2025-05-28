package com.exhibition.config;

import com.exhibition.dto.auth.AdminMainEntityDto;
import com.exhibition.dto.auth.MemberMainEntityDto;
import com.exhibition.dto.user.CustomAdminDetail;
import com.exhibition.dto.user.CustomMemberDetail;
import com.exhibition.exception.AuthenticationFailedException;
import com.exhibition.exception.AuthorizationFailedException;
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
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Value("${jwt.secret}")
    private String secretBase64;
    private SecretKey secretKey;

    private final JwtUtil jwtUtil;

    // ✅ 路徑白名單（避免誤觸 Swagger 等非保護資源）
    private static final String[] WHITELIST_PATHS = {
            "/v3/api-docs", "/swagger-ui", "/swagger-resources", "/webjars",
            "/member/api/sys", "/admin/api/sys", "/api/base", "/endpoint"
    };

    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @PostConstruct
    public void init() {
        this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretBase64));
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException, IOException {

        String path = request.getRequestURI();

        // ✅ 1. 白名單放行
        if (Arrays.stream(WHITELIST_PATHS).anyMatch(path::startsWith)) {
            filterChain.doFilter(request, response);
            return;
        }

        boolean isRefreshRequest = path.contains("/refreshToken");

        // ✅ 2. 取得 JWT token
        String token = getAuthToken(request, isRefreshRequest);
        if (token == null || token.isBlank()) {
            // 沒帶 token 的 request（例如 /login、/register）直接放行
            filterChain.doFilter(request, response);
            return;
        }

        try {
            // ✅ 3. 驗證與提取資料
            if (jwtUtil.isTokenExpired(token)) {
                clearAuthCookies(response);
                throw new AuthenticationFailedException("Token 已過期");
            }

            String subject = jwtUtil.extractSubject(token);
            String userType = jwtUtil.extractUserType(token);
            String userId = jwtUtil.extractUserId(token);
            List<String> roles = jwtUtil.extractRoles(token);

            if (subject == null || userType == null) {
                throw new AuthenticationFailedException("Token 中缺少必要資訊");
            }

            boolean isValidToken = isRefreshRequest
                    ? jwtUtil.validateRefreshToken(token)
                    : jwtUtil.validateToken(token, userType);

            if (!isValidToken) {
                throw new AuthenticationFailedException("Token 驗證失敗");
            }

            // ✅ 4. 設定 Spring Security 的使用者資訊
            if (SecurityContextHolder.getContext().getAuthentication() == null) {
                List<GrantedAuthority> authorities = roles.stream()
                        .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                        .collect(Collectors.toList());

                if ("ADMIN".equals(userType)) {
                    AdminMainEntityDto admin = new AdminMainEntityDto();
                    admin.setEmail(subject);
                    admin.setAccount(userId);
                    SecurityContextHolder.getContext().setAuthentication(
                            new UsernamePasswordAuthenticationToken(new CustomAdminDetail(admin, authorities), null, authorities)
                    );
                } else if ("MEMBER".equals(userType)) {
                    MemberMainEntityDto member = new MemberMainEntityDto();
                    member.setEmail(subject);
                    member.setMemberId(userId);
                    SecurityContextHolder.getContext().setAuthentication(
                            new UsernamePasswordAuthenticationToken(new CustomMemberDetail(member, authorities), null, authorities)
                    );
                } else {
                    throw new AuthorizationFailedException("無效的 userType：" + userType);
                }
            }

        } catch (AuthenticationFailedException | AuthorizationFailedException ex) {
            log.warn("[Jwt Filter] 驗證失敗: {}", ex.getMessage());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        } catch (Exception ex) {
            log.error("[Jwt Filter] 發生未知錯誤", ex);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return;
        }

        filterChain.doFilter(request, response);
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
        return (authHeader != null && authHeader.startsWith("Bearer "))
                ? authHeader.substring(7)
                : null;
    }

    private void clearAuthCookies(HttpServletResponse response) {
        Cookie cookie = new Cookie("AUTH_TOKEN", null);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }
}
