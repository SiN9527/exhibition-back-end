package com.exhibition.exception;


import com.exhibition.enumerate.ServiceExceptionReason;
import com.exhibition.recover.Rollback;
import lombok.Getter;

import java.util.List;

@Getter
public class ServiceUnRecoverableException extends ServiceException {
    // 外部狀態或程式錯誤造成、必須透過修改程式恢復、一定通報系統alert

    private final List<Rollback> rollbacks;

    public ServiceUnRecoverableException(List<Rollback> rollbacks, String breifMessage, ServiceExceptionReason exceptionReason) {
        super(breifMessage, exceptionReason);
        this.rollbacks = rollbacks;
    }

    public ServiceUnRecoverableException(List<Rollback> rollbacks, String breifMessage, ServiceExceptionReason exceptionReason, Throwable throwable) {
        super(breifMessage, exceptionReason, throwable);
        this.rollbacks = rollbacks;
    }

    public ServiceUnRecoverableException(List<Rollback> rollbacks, String breifMessage, ServiceExceptionReason exceptionReason, String detailMessage, String reasonDesc) {
        super(breifMessage, exceptionReason, detailMessage, reasonDesc);
        this.rollbacks = rollbacks;
    }

    public ServiceUnRecoverableException(List<Rollback> rollbacks, String breifMessage, ServiceExceptionReason exceptionReason, String detailMessage, String reasonDesc, Throwable throwable) {
        super(breifMessage, exceptionReason, detailMessage, reasonDesc, throwable);
        this.rollbacks = rollbacks;
    }
}
