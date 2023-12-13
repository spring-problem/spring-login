package com.example.springlogin.global.exception;

import com.example.springlogin.global.exception.handler.AuthExceptionHandler;

public class InCookieNotFoundUser extends AuthException {
    public InCookieNotFoundUser(Throwable cause) {
        super(cause);
    }
}
