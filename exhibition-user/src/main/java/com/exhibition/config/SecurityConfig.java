package com.exhibition.config;

import com.exhibition.utils.JwtUtil;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity // 開啟 @PreAuthorize 等權限註解功能（如果有用到）
public class SecurityConfig {

    // 注入自定義的 JwtUtil，用來處理 JWT 的相關邏輯
    @Resource
    private JwtUtil jwtUtil;
    private final JwtAuthenticationFilter jwtFilter;
    @Resource
    private CorsConfigurationSource source;

    private static final String[] SWAGGER_WHITELIST = {
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/swagger-resources/**",
            "/webjars/**",
            "/api/member/sys/**",
            "/api/admin/sys/**",
            "/api/base/**",
            "/endpoint/**"




    };

    public SecurityConfig(JwtUtil jwtUtil, JwtAuthenticationFilter jwtFilter, CorsConfigurationSource source) {
        this.jwtUtil = jwtUtil;
        this.jwtFilter = jwtFilter;
        this.source = source;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable) // 禁用 CSRF（因為是前後端分離 + JWT）
                .authorizeHttpRequests(auth -> auth
                        // 白名單完全放行
                        .requestMatchers(SWAGGER_WHITELIST).permitAll()
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll() // CORS 預檢請求放行
                        // 其他請求一律需要認證
                        .anyRequest().authenticated()
                )
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // 無狀態，適合 JWT
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class) // 加上 JWT 驗證過濾器
                .build();
    }

    /**
     * 提供 AuthenticationManager（登入時使用）
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    /**
     * 密碼加密器
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}


