package com.exhibition.aspect;

public class TraceContextHolder {

    private static final ThreadLocal<String> traceKeyHolder = new ThreadLocal<>();

    public static void setTraceKey(String traceKey) {
        traceKeyHolder.set(traceKey);
    }

    public static String getTraceKey() {
        return traceKeyHolder.get();
    }

    public static void clear() {
        traceKeyHolder.remove();
    }
}