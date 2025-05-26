package com.exhibition.exception.payload;


import com.exhibition.enumerate.ProcessReturnStatus;
import com.exhibition.enums.ErrorCode;
import com.exhibition.exception.*;
import lombok.Getter;

import java.util.List;

@Getter

public final class ProcessPayload<T> extends AbstractProcessPayload<T> {

    private ProcessPayload(T result, boolean isSuccess, String snackbarMessage, ProcessReturnStatus status,
                           List<String> invalidFields, List<String> invalidReasons, RuntimeException cause) {
        super(result, isSuccess, snackbarMessage,  status, invalidFields, invalidReasons, cause);
    }


    public static <T> ProcessPayload<T> success(T result) {
        return new ProcessPayload<T>(result, true, null, null, null, null, null);
    }

    public static <T> ProcessPayload<T> fail(T result) {
        return new ProcessPayload<T>(result, true, null, null, null, null, null);
    }

    public static <T> ProcessPayload<T> invalidArg(String msg, List<String> invalidFields, List<String> invalidReason) {
        return new ProcessPayload<T>(null, false, msg,ProcessReturnStatus.WARN, invalidFields, invalidReason, new ArgumentValidationException(invalidFields, invalidReason));
    }

    public static <T> ProcessPayload<T> invalidArg(String msg, List<String> invalidFields, List<String> invalidReason, ArgumentValidationException ex) {
        return new ProcessPayload<T>(null, false, msg, ProcessReturnStatus.WARN, invalidFields, invalidReason, ex);
    }

    public static <T> ProcessPayload<T> tryLater(String msg, FailedAndForgetException ex) {
        return new ProcessPayload<T>(null, false, null,ProcessReturnStatus.TRY_LATER, null, null, ex);
    }

    public static <T> ProcessPayload<T> prevent(String msg, LogicalProhibitedException ex) {
        return new ProcessPayload<T>(null, false, msg, ProcessReturnStatus.PREVENT, null, null, ex);
    }

    public static <T> ProcessPayload<T> softAbort(String msg, ServiceRecoverableException ex) {
        return new ProcessPayload<T>(null, false, msg, ProcessReturnStatus.SOFT_ABORT, null, null, ex);
    }

    public static <T> ProcessPayload<T> softError(String msg, ServiceUnRecoverableException ex) {
        return new ProcessPayload<T>(null, false, msg, ProcessReturnStatus.SOFT_ERROR, null, null, ex);
    }


    // 1. 新增「未授權（Unauthorized）」– HTTP 401
    public static <T> ProcessPayload<T> unauthorized(String msg, RuntimeException ex) {
        return new ProcessPayload<>(null, false, msg, ProcessReturnStatus.UNAUTHORIZED, null, null, ex);
    }

    // 2. 新增「禁止存取（Forbidden）」– HTTP 403
    public static <T> ProcessPayload<T> authorizedFail(String msg, RuntimeException ex) {
        return new ProcessPayload<>(null, false, msg, ProcessReturnStatus.FORBIDDEN, null, null, ex);
    }

    public static <T> ProcessPayload<T> authenticateFail(String msg, RuntimeException ex) {
        return new ProcessPayload<>(null, false, msg, ProcessReturnStatus.AUTHENTICATE_FAILED, null, null, ex);
    }

    public static <T> ProcessPayload<T> unknownError(String msg, RuntimeException ex) {
        return new ProcessPayload<T>(null, false, msg, ProcessReturnStatus.UNKNOWN, null, null, ex);
    }

    public T elseThrow() {
        if (isSuccess()) {
            return result;
        }
        throw cause;
    }

    public ProcessPayload<T> setSnackbarIfArgInvalide(String snackbarMessage) {
        if (this.isArgInvalid()) {
            this.snackbarMessage = snackbarMessage;
        }
        return this;
    }

    public ProcessPayload<T> setSnackbarIfPrevent(String snackbarMessage) {
        if (this.isPrevent()) {
            this.snackbarMessage = snackbarMessage;
        }
        return this;
    }

    public ProcessPayload<T> setSnackbarIfTemporary(String snackbarMessage) {
        if (this.isTemporary()) {
            this.snackbarMessage = snackbarMessage;
        }
        return this;
    }



    public ProcessPayload<T> setSnackbarIfAborted(String snackbarMessage) {
        if (this.isAborted()) {
            this.snackbarMessage = snackbarMessage;
        }
        return this;
    }

    public ProcessPayload<T> setSnackbarIfSoftError(String snackbarMessage) {
        if (this.isSoftError()) {
            this.snackbarMessage = snackbarMessage;
        }
        return this;
    }

    public ProcessPayload<T> throwIfServiceException() {
        if (this.isSoftError() || this.isAborted()) {
            throw this.cause;
        }
        return this;
    }

}
