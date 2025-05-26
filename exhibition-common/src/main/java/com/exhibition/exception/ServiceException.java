package com.exhibition.exception;


import com.exhibition.enumerate.ServiceExceptionReason;
import lombok.Data;

import java.time.Instant;

@Data
public class ServiceException extends RuntimeException {

    // 系統執行function 後才會發生的例外，排除參數本身驗證沒過、API 已廢棄
    private Instant timestamp;
    private String briefMessage;
    private String reason; // optional
    private ServiceExceptionReason exceptionReason;

    public ServiceException(String breifMessage, ServiceExceptionReason exceptionReason) {
        this.briefMessage = breifMessage;
        this.timestamp = Instant.now();
        this.exceptionReason = exceptionReason;
    }

    public ServiceException(String breifMessage, ServiceExceptionReason exceptionReason, Throwable cause) {
        super(cause);
        this.briefMessage = breifMessage;
        this.timestamp = Instant.now();
        this.exceptionReason = exceptionReason;
    }

    public ServiceException(String breifMessage, ServiceExceptionReason exceptionReason, String detailMessage, String reasonDesc) {
        super(detailMessage);
        this.briefMessage = breifMessage;
        this.timestamp = Instant.now();
        this.exceptionReason = exceptionReason;
        this.reason = reasonDesc;
    }

    public ServiceException(String breifMessage, ServiceExceptionReason exceptionReason, String detailMessage, String reasonDesc, Throwable cause) {
        super(detailMessage, cause);
        this.briefMessage = breifMessage;
        this.timestamp = Instant.now();
        this.exceptionReason = exceptionReason;
        this.reason = reasonDesc;
    }


}
