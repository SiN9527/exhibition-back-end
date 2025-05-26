package com.exhibition.exception;


import com.exhibition.enumerate.ServiceExceptionReason;

public class LogicalProhibitedException extends ServiceException {

    public LogicalProhibitedException(String briefMessage) {
        super(briefMessage, ServiceExceptionReason.BUSINESS_LOGICAL_PROHIBITED);
    }
}
