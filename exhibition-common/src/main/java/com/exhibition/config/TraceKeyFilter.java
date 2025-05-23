package com.exhibition.config;

import com.exhibition.aspect.TraceContextHolder;
import jakarta.servlet.Filter;
import org.slf4j.MDC;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import java.util.UUID;

@Configuration
public class TraceKeyFilter {

    @Bean
    @Order(-1)
    public Filter traceKeyConfig() {
        return (req, res, chain) -> {
            String traceKey = UUID.randomUUID().toString();
            TraceContextHolder.setTraceKey(traceKey);
            // 可選：加入到 MDC 中，log 輸出會更方便
            MDC.put("traceKey", traceKey);
            chain.doFilter(req, res);
            TraceContextHolder.clear();
            MDC.clear(); // 清除 log context
        };
    }

}
