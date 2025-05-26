package com.exhibition.exception;

import com.hititoff.enumerate.ServiceExceptionReason;
import lombok.Getter;

@Getter
public class ServiceRecoverableException extends ServiceException {
    // 外部狀態造成、可不透過修改程式恢復、(選)通報系統alert
    private Boolean alertRequired;

    public ServiceRecoverableException(String breifMessage, ServiceExceptionReason exceptionReason, boolean alertRequired) {
        super(breifMessage, exceptionReason);
        this.alertRequired = alertRequired;
    }

    public ServiceRecoverableException(String breifMessage, ServiceExceptionReason exceptionReason, boolean alertRequired, Throwable throwable) {
        super(breifMessage, exceptionReason, throwable);
        this.alertRequired = alertRequired;
    }

    public ServiceRecoverableException(String breifMessage, ServiceExceptionReason exceptionReason, String detailMessage, String reasonDesc, boolean alertRequired) {
        super(breifMessage, exceptionReason, detailMessage, reasonDesc);
        this.alertRequired = alertRequired;
    }

    public ServiceRecoverableException(String breifMessage, ServiceExceptionReason exceptionReason, String detailMessage, String reasonDesc, boolean alertRequired, Throwable throwable) {
        super(breifMessage, exceptionReason, detailMessage, reasonDesc, throwable);
        this.alertRequired = alertRequired;
    }
}
