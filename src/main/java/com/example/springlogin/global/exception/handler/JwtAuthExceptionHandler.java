package com.example.springlogin.global.exception.handler;

import com.example.springlogin.global.util.CookieUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JwtAuthExceptionHandler implements AuthExceptionHandler {

    final String accessCookieKey;
    final String refreshCookieKey;

    @Override
    public void handleAuthException(HttpServletRequest request, HttpServletResponse response) {
        CookieUtil.expireCookie(response, accessCookieKey);
        CookieUtil.expireCookie(response, refreshCookieKey);
    }
}
