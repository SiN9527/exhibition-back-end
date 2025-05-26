package com.exhibition.exception.payload;


import com.exhibition.enumerate.ProcessReturnStatus;
import lombok.Getter;

@Getter
public final class ControllerProcessPayload<T> extends AbstractProcessPayload<T> {

    private ControllerProcessPayload(final String snackbarMessage, final ProcessReturnStatus status) {
        super(null, false, snackbarMessage, status, null, null, null);
    }

    public static <T> ControllerProcessPayload<T> fatal(final String snackbarMessage) {
        return new ControllerProcessPayload<T>(snackbarMessage, ProcessReturnStatus.CONTROLLER_FATAL_ERROR);
    }

    public static <T> ControllerProcessPayload<T> softAbort(final String snackbarMessage) {
        return new ControllerProcessPayload<T>(snackbarMessage, ProcessReturnStatus.CONTROLLER_ABORT);
    }

    public static <T> ControllerProcessPayload<T> softError(final String snackbarMessage) {
        return new ControllerProcessPayload<T>(snackbarMessage, ProcessReturnStatus.CONTROLLER_ERROR);
    }
}