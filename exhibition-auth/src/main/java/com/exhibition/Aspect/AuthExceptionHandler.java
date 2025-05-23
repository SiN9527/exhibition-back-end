// src/main/java/com/hititoff/config/GlobalExceptionHandler.java
package com.exhibition.Aspect;

import com.exhibition.exception.*;
import com.exhibition.monitor.event.UnknowThrowableAlertEvent;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@Slf4j
@RestControllerAdvice
@AllArgsConstructor
public class AuthExceptionHandler {

    private final ApplicationEventPublisher eventPublisher;


    @ExceptionHandler(Throwable.class)
    public ResponseEntity<?> handleThrowableException(Throwable ex) {
        log.error(ex.getMessage(), ex);
        UnknowThrowableAlertEvent event = UnknowThrowableAlertEvent.of(ex);
        eventPublisher.publishEvent(event);
        return ResponseEntity.badRequest().body(ControllerProcessPayload.fatal(ex.getMessage() == null ? "系統錯誤" : ex.getMessage()));
    }


    @ExceptionHandler(ArgumentValidationException.class)
    public ResponseEntity<ProcessPayload<Void>> handleArgVal(ArgumentValidationException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ProcessPayload.invalidArg(
                        "欄位驗證失敗", ex.getInvalidFields(), ex.getInvalidReasons()
                ));
    }

    @ExceptionHandler({UnauthenticatedException.class})
    public ResponseEntity<ProcessPayload<Void>> handleUnauthorized(UnauthenticatedException ex) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(ProcessPayload.unauthorized(
                        ex.getMessage(), ex
                ));
    }

    @ExceptionHandler(AuthenticationFailedException.class)
    public ResponseEntity<ProcessPayload<Void>> handleAuthentication(AuthenticationFailedException ex) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(ProcessPayload.authenticateFail(
                        ex.getMessage(), ex
                ));
    }

    @ExceptionHandler(AuthorizationFailedException.class)
    public ResponseEntity<ProcessPayload<Void>> handleAuthorized(AuthorizationFailedException ex) {
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(ProcessPayload.authorizedFail(
                        ex.getMessage(), ex
                ));
    }


    @ExceptionHandler(FailedAndForgetException.class)
    public ResponseEntity<ProcessPayload<Void>> handleTryLater(FailedAndForgetException ex) {
        return ResponseEntity
                .status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(ProcessPayload.tryLater(ex.getMessage(), ex));
    }

    @ExceptionHandler(ServiceRecoverableException.class)
    public ResponseEntity<ProcessPayload<Void>> handleSoftAbort(ServiceRecoverableException ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(ProcessPayload.softAbort(ex.getMessage(), ex));
    }

    @ExceptionHandler(ServiceUnRecoverableException.class)
    public ResponseEntity<ProcessPayload<Void>> handleSoftError(ServiceUnRecoverableException ex) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ProcessPayload.softError(ex.getMessage(), ex));

    }


}
