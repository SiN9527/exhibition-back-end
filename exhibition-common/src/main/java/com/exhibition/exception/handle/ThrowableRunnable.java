package com.exhibition.exception.handle;

@FunctionalInterface
public interface ThrowableRunnable<T> {
    T run() ;
}