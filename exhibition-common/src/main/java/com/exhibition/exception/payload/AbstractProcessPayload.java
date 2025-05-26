package com.exhibition.exception.payload;


import com.exhibition.enumerate.ProcessReturnStatus;
import com.exhibition.enums.ErrorCode;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
public class AbstractProcessPayload<T> {

    protected final T result;
    @Setter
    protected String snackbarMessage;
    protected final boolean isSuccess;

    protected final ProcessReturnStatus status;
    @Setter
    protected List<String> invalidFields;
    @Setter
    protected List<String> invalidReason;
    @JsonIgnore
    protected final RuntimeException cause;

    protected AbstractProcessPayload(T result, boolean isSuccess, String snackbarMessage, ProcessReturnStatus status,
                                     List<String> invalidFields, List<String> invalidReasons, RuntimeException cause) {
        this.result = result;
        this.isSuccess = isSuccess;
        this.snackbarMessage = snackbarMessage;
        this.status = status;
        this.invalidFields = invalidFields;
        this.invalidReason = invalidReasons;
        this.cause = cause;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public boolean isArgInvalid() {
        return invalidFields!=null && !invalidFields.isEmpty();
    }

    public boolean isTemporary() {
        return ProcessReturnStatus.TRY_LATER.equals(status);
    }

    public boolean isPrevent() {
        return ProcessReturnStatus.PREVENT.equals(status);
    }

    public boolean isAborted() {
        return ProcessReturnStatus.SOFT_ABORT.equals(status);
    }

    public boolean isSoftError() {
        return ProcessReturnStatus.SOFT_ERROR.equals(status);
    }

}
