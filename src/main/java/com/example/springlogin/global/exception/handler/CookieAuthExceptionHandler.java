package com.example.springlogin.global.exception.handler;

import com.example.springlogin.global.util.CookieUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CookieAuthExceptionHandler implements  AuthExceptionHandler{
    // advide 는 그냥 에러만 잡아 온다 ! (AuthException 의 오류를 잡아오고 InCookieNotFoundUser(Cookie controller 에 있는) 는 상속 받았으니 에러가 advice 에서 잡힌다)
    // advice는 AuthExceptionHandler 인터페이스를 호출하고 Configuration 에서 등록된 구현체인 CookieAuthExceptionHandler 로 오버라이드 되서 이 함수가 호출된다 여기서 로직을 구현
    private final String loginCookieName;

    @Override
    public void handleAuthException(HttpServletRequest request, HttpServletResponse response) {
        CookieUtil.expireCookie(response, loginCookieName);
    }
}
