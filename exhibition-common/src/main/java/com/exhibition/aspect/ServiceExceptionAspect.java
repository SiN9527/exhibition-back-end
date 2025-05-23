package com.exhibition.aspect;

import com.exhibition.monitor.event.ServiceExceptionAlertEvent;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
@AllArgsConstructor
public class ServiceExceptionAspect {

    private final ApplicationEventPublisher eventPublisher;

    @Around("@within(org.springframework.stereotype.Service)")
    public Object publishExceptionEvent(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            return joinPoint.proceed();
        } catch (ServiceRecoverableException ex) {
            if (ex.getAlertRequired()) {
                ServiceExceptionAlertEvent event = ServiceExceptionAlertEvent.of(ex, joinPoint);
                eventPublisher.publishEvent(event);
            }
            throw ex;
        } catch (ServiceUnRecoverableException ex) {
            ServiceExceptionAlertEvent event = ServiceExceptionAlertEvent.of(ex, joinPoint);
            eventPublisher.publishEvent(event);
            throw ex;
        } catch (Throwable throwable) {
            // TODO
            // 本來就預期非custom Exception 會一路丟到外面然後 stack trace會寫進log裡面
            // 如果外來有需要特別處理沒handle到的 throwable的stackTrace
            // 就將邏輯寫在這裡
            // 通常會走到這裡的就是code 沒寫好或是 exception沒去handle
            throw throwable;
        }
    }
}
