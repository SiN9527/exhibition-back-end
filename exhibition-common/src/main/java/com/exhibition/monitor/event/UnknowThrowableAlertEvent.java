package com.exhibition.monitor.event;

import lombok.Getter;

import java.util.Arrays;
import java.util.stream.StreamSupport;

@Getter
public class UnknowThrowableAlertEvent {
    private final String exceptionType;
    private final String message;
    private final String atClass;
    private final String atMethod;
    private final int line;
    private final String[] stackTraceSummary;
    private final StackTraceElement[] stackTrace; // max preserve 5

    public UnknowThrowableAlertEvent(String exceptionType, String message, String atClass,
                                     String atMethod, int line, String[] stackTraceSummary,
                                     StackTraceElement[] stackTrace) {
        this.exceptionType = exceptionType;
        this.message = message;
        this.atClass = atClass;
        this.atMethod = atMethod;
        this.line = line;
        this.stackTraceSummary = stackTraceSummary;
        this.stackTrace = stackTrace;
    }

    public static UnknowThrowableAlertEvent of(Throwable throwable) {
        String exceptionType = throwable.getClass().getName();
        String message = throwable.getMessage();
        String atClass = null;
        String atMethod = null;
        Integer atLine = null;
        StackTraceElement[] sts = throwable.getStackTrace();
        if (sts.length > 0) {
            StackTraceElement top = sts[0]; // 最上層發生點
            atClass = top.getClassName();
            atMethod = top.getMethodName();
            atLine = top.getLineNumber();
        }
        sts = StreamSupport.stream(Arrays.spliterator(sts), false).limit(5).toArray(StackTraceElement[]::new);
        String[] stackSummary = Arrays.stream(sts).map(StackTraceElement::toString).toArray(String[]::new);
        return new UnknowThrowableAlertEvent(exceptionType, message, atClass, atMethod, atLine, stackSummary, sts);
    }
}
