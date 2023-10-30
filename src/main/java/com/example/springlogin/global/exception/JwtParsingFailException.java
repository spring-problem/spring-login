package com.example.springlogin.global.exception;

public class JwtParsingFailException extends BusinessException {

    public JwtParsingFailException(Throwable cause) {
        super(cause);
    }
}
