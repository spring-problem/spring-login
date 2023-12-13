package com.example.springlogin.global.exception.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthExceptionHandler {

    void handleAuthException(HttpServletRequest request, HttpServletResponse response);
}
