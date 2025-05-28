package com.exhibition.exception;

public class UnauthenticatedException extends RuntimeException {
    public UnauthenticatedException() {
        super("使用者未登入或憑證已失效");
    }
}
