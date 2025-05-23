package com.exhibition.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

@Configuration
@EnableWebSecurity
@EnableMethodSecurity // 開啟 @PreAuthorize 等權限註解功能（如果有用到）
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtFilter;

    private final Logger log = LoggerFactory.getLogger(SecurityConfig.class);

    // Swagger + Open API 白名單路徑
    private static final String[] SWAGGER_WHITELIST = {
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/swagger-resources/**",
            "/webjars/**"
    };

    public SecurityConfig(JwtAuthenticationFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    /**
     * 單一 SecurityFilterChain：統一管理所有安全策略
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable) // 禁用 CSRF（因為是前後端分離 + JWT）
                .authorizeHttpRequests(auth -> auth
                        // 白名單完全放行
                        .requestMatchers(SWAGGER_WHITELIST).permitAll()
                        .requestMatchers("/endpoint/user/register").permitAll() // 開放本地註冊
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
