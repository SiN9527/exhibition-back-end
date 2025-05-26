package com.exhibition.exception;


import com.exhibition.enumerate.ServiceExceptionReason;

public class FailedAndForgetException extends ServiceException {
    // 非外部造成、暫時、沒造成狀態異常

    public FailedAndForgetException(String briefMessage) {
        super(briefMessage, ServiceExceptionReason.IGNORE_AND_FORGET_EXCEPTION);
    }

    public FailedAndForgetException(String breifMessage, ServiceExceptionReason exceptionReason) {
        super(breifMessage, exceptionReason);
    }

    public FailedAndForgetException(String breifMessage, ServiceExceptionReason exceptionReason, String detailMessage, String reasonDesc) {
        super(breifMessage, exceptionReason, detailMessage, reasonDesc);
    }

}
