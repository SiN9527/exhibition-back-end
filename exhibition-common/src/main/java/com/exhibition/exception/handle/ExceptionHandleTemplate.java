package com.exhibition.exception.handle;


import com.exhibition.exception.payload.ProcessPayload;

public abstract class ExceptionHandleTemplate<T> {

    protected final ThrowableRunnable<T> runnable;

    protected ExceptionHandleTemplate(ThrowableRunnable<T> runnable) {
        this.runnable = runnable;
    }

    public abstract ProcessPayload<T> execute() throws Exception;
}
