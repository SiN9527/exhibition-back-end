package com.exhibition.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Optional;

@EnableJpaAuditing
@Configuration
public class JpaAuditConfig {
    @Bean
    public AuditorAware<String> auditorProvider() {
        // TODO 預設JPA audit createdById欄位填入職
        return () -> Optional.of("SYSTEM");

        // TODO 後面可以考慮使用SecuirtyContext 取得當前authentication 得到userId
//        return () -> Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
//                .map(Authentication::getName);
    }
}
