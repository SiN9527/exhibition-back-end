package com.exhibition.exception;

public class AuthorizationFailedException extends RuntimeException {
    public AuthorizationFailedException(String briefMessage) {
        super(briefMessage !=null?briefMessage:"缺少存取權限");
    }
}
