package com.exhibition.aspect;

import com.exhibition.monitor.event.UnknowThrowableAlertEvent;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.TypeMismatchException;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.HandlerMethod;

import java.util.List;

@Slf4j
@ControllerAdvice
@AllArgsConstructor
public class GlobalExceptionHandler {

    private final ApplicationEventPublisher eventPublisher;

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<?> handleThrowableException(Throwable ex) {
        log.error(ex.getMessage(), ex);
        UnknowThrowableAlertEvent event = UnknowThrowableAlertEvent.of(ex);
        eventPublisher.publishEvent(event);
        return ResponseEntity.badRequest().body(ControllerProcessPayload.fatal("系統錯誤"));
    }

    @ExceptionHandler(ConversionFailedException.class)
    public ResponseEntity<?> handleConversionFailedException(ConversionFailedException ex) {
        return ResponseEntity.ok(ProcessPayload.invalidArg("請求url參數錯誤", List.of(), List.of()));
    }

    @ExceptionHandler(TypeMismatchException.class)
    public ResponseEntity<?> handleTypeMismatchException(TypeMismatchException ex) {
        return ResponseEntity.ok(ProcessPayload.invalidArg("請求url參數錯誤", List.of(), List.of()));
    }

    // handle for JSR_303
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationException(MethodArgumentNotValidException ex) {
        BindingResult result = ex.getBindingResult();
        // 擷取錯誤資訊
        List<String> invalidFields = result.getFieldErrors().stream().map(FieldError::getField).toList();
        List<String> invalidReasons = result.getFieldErrors().stream().map(FieldError::getDefaultMessage).toList();
        return ResponseEntity.ok(ProcessPayload.invalidArg("參數錯誤", invalidFields, invalidReasons));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<?> handleConstraintViolation(ConstraintViolationException ex, HandlerMethod handlerMethod) {
        List<String> invalidFields = ex.getConstraintViolations().stream()
                .map(ConstraintViolation::getPropertyPath)
                .map(Path::toString).toList();
        List<String> invalidReasons = ex.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage).toList();
        return ResponseEntity.ok(ProcessPayload.invalidArg("參數錯誤", invalidFields, invalidReasons));
    }

    @ExceptionHandler(ServiceRecoverableException.class)
    public ResponseEntity<?> handleServiceRecoverableException(ServiceRecoverableException ex, HandlerMethod handlerMethod) {
        log.error(ex.getMessage(), ex);
        return ResponseEntity.badRequest().body(ControllerProcessPayload.softAbort("系統錯誤"));
    }

    @ExceptionHandler(ServiceUnRecoverableException.class)
    public ResponseEntity<?> handleServiceUnRecoverableException(ServiceUnRecoverableException ex, HandlerMethod handlerMethod) {
        log.error(ex.getMessage(), ex);
        return ResponseEntity.badRequest().body(ControllerProcessPayload.softError("系統錯誤"));
    }
}
