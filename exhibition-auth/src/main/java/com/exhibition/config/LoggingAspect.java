package com.exhibition.config;

import com.exhibition.utils.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.UUID;

@Aspect
@Component
@Slf4j
public class LoggingAspect {

    private final HttpServletRequest request;
   private  final JwtService jwtService;
    // 使用 LoggerFactory 建立 Logger 實例，傳入當前類別作為參數
    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);


    public LoggingAspect(HttpServletRequest request, JwtService jwtService) {
        this.request = request;

        this.jwtService = jwtService;
    }

    /**
     * 定義 Pointcut，攔截 Controller 層的所有方法
     */
    @Pointcut("execution(* com.exhibition.controller..*(..))")
    public void controllerMethods() {
    }

    /**
     * 在 Controller 方法執行前記錄請求參數
     */
    @Before("controllerMethods()")
    public void logRequestDetails(JoinPoint joinPoint) {
        String traceId = UUID.randomUUID().toString(); // 生成請求唯一識別碼
        String token = request.getHeader("Authorization"); // 從 Header 取出 Token

        String email = "UNKNOWN EMAIL";
        String username = "ANONYMOUS";

        if (token != null && token.startsWith("Bearer ")) {
            String jwt = token.substring(7);
            email = JwtUtil.extractSubject(jwt,jwtService.getSecretKey());
            username = JwtUtil.extractUserName(jwt,jwtService.getSecretKey());
        }

        logger.info(" [API請求] traceId={} | Email={} | User={} | Method={} | Args={}",
                traceId, email, username, joinPoint.getSignature(), Arrays.toString(joinPoint.getArgs()));
    }

    /**
     * 在 Controller 方法發生異常時記錄
     */
    @AfterThrowing(pointcut = "controllerMethods()", throwing = "exception")
    public void logError(JoinPoint joinPoint, Throwable exception) {
        logger.error("[API異常] Method={} | Message={ } | Exception={}",
                joinPoint.getSignature(), exception.getMessage(), exception);
    }
}
