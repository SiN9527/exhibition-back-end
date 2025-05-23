package com.exhibition.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

//    @Autowired
//    private final JwtAuthenticationFilter jwtFilter;

    private final Logger log = LoggerFactory.getLogger(SecurityConfig.class);

//    private final RequestMatcher endpointMatcher = new RequestMatcher() {
//        @Override
//        public boolean matches(HttpServletRequest request) {
//            String url = request.getRequestURI();
//            String remoteHost = request.getRemoteHost();
//            return url.startsWith("/endpoint") && remoteHost.startsWith("127.0.0.1");
//        }
//    };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // TODO 後續可配置 csrs ,cors ,JWT token filter
        return http.securityMatcher("/open/api/**")
                .authorizeHttpRequests(auth -> auth.requestMatchers("/**").permitAll())
                .securityMatcher("/endpoint/**")
                .authorizeHttpRequests(auth -> auth.requestMatchers("/**").permitAll())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.NEVER))
//                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .csrf(AbstractHttpConfigurer::disable)
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}