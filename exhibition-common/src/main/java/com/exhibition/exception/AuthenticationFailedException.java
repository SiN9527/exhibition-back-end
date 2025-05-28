package com.exhibition.exception;

public class AuthenticationFailedException extends RuntimeException {
    public AuthenticationFailedException(String briefMessage) {
        super( briefMessage !=null?briefMessage:"帳號或密碼錯誤");
    }
}