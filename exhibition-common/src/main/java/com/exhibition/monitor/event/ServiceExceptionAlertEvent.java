package com.exhibition.monitor.event;


import com.exhibition.enumerate.ServiceExceptionReason;
import com.exhibition.exception.ServiceException;
import lombok.Data;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;

import java.time.Instant;

@Data
public class ServiceExceptionAlertEvent {

    private final ServiceException serviceException;
    private final Instant timestamp;
    private final String ServiceName;
    private final String methodName;
    private final ServiceExceptionReason reason;

    public static ServiceExceptionAlertEvent of(ServiceException ex, ProceedingJoinPoint joinPoint) {
        Signature signature = joinPoint.getSignature();
        String methodName = signature.getName();
        String className = signature.getDeclaringType().getName();
        return new ServiceExceptionAlertEvent(
                ex, ex.getTimestamp(), className, methodName, ex.getExceptionReason()
        );
    }

}
