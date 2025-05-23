package com.exhibition.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Profile("dev")
@Slf4j
public class DevLoggingAspect {

    @Around("execution(* com.exhibition..service..*(..)) || execution(* com.exhibition..repository..*(..))")
    public Object logServiceAndRepositoryCalls(ProceedingJoinPoint joinPoint) throws Throwable {
        String className = joinPoint.getSignature().getDeclaringTypeName();
        String methodName = joinPoint.getSignature().getName();
        String traceId = TraceContextHolder.getTraceKey();
        log.info("[traceId={}] Calling {}.{}", traceId, className, methodName);
        Object result = joinPoint.proceed();
        log.info("[traceId={}] end {}.{}", traceId, className, methodName);
        return result;
    }
}