package com.example.springlogin.global.exception;

public class JwtParsingFailException extends AuthException {

    public JwtParsingFailException(Throwable cause) {
        super(cause);
    }
}
