package com.example.springlogin.global.exception;

public class BusinessException extends RuntimeException {

    public BusinessException(Throwable cause) {
        super(cause);
    }

    public BusinessException(String message) {
        super(message);
    }
}
