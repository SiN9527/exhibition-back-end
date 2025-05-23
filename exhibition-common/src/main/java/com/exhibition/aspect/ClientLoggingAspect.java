package com.exhibition.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class ClientLoggingAspect {

    /**
     * 切點說明：
     * 1. execution(* com.hititoff..serviceAdapter..*(..))
     *    → 攔截 com.hititoff 任何子 package 下，serviceAdapter package 內的所有方法。
     * 2. && !execution(* com.hititoff..serviceAdapter.client..*(..))
     *    → 排除 com.hititoff 任何子 package 下，serviceAdapter.client package 內的所有方法。
     *
     * 這樣就能精準只對 serviceAdapter 裡自己寫的 Adapter 方法打日誌，
     * 而不會誤攔截到其他不相關的 bean（例如 Spring Data 的內部 RepositoryFragments）。
     */
    @Around("execution(* com.exhibition..serviceAdapter..*(..))"
            + " && !execution(* com.exhibition..serviceAdapter.client..*(..))")
    public Object logServiceAndRepositoryCalls(ProceedingJoinPoint joinPoint) throws Throwable {
        // 取得類別與方法名稱
        String className  = joinPoint.getSignature().getDeclaringTypeName();
        String methodName = joinPoint.getSignature().getName();
        // 取得 Trace ID（假設你有這個工具）
        String traceId    = TraceContextHolder.getTraceKey();

        // 方法執行前日誌
        log.info("[traceId={}] ▶▶ Enter {}.{}", traceId, className, methodName);
        // 真正執行目標方法
        Object result = joinPoint.proceed();
        // 方法執行後日誌
        log.info("[traceId={}] ◀◀ Exit  {}.{}",  traceId, className, methodName);

        return result;
    }
}
