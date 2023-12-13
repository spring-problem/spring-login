package com.example.springlogin.global.exception.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SessionAuthExceptionHandler implements AuthExceptionHandler{
    private final String loginBySession;
    @Override
    public void handleAuthException(HttpServletRequest request, HttpServletResponse response) {
    }
}
