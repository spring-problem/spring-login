package com.example.springlogin.global.advice;

import com.example.springlogin.domain.member.controller.MemberController;
import com.example.springlogin.domain.member.controller.MemberJwtController;
import com.example.springlogin.global.exception.JwtParsingFailException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Arrays;

@Slf4j
@ControllerAdvice
public class ExceptionControllerAdvice {

    private final String authCookieName;
    private final String refreshCookieName;

    public ExceptionControllerAdvice(@Value("${auth.jwt.access-cookie-key}") String authCookieName,
                                     @Value("${auth.jwt.refresh-cookie-key}") String refreshCookieName) {
        this.authCookieName = authCookieName;
        this.refreshCookieName = refreshCookieName;
    }

    @ExceptionHandler(JwtParsingFailException.class)
    public String handleJwtParsingFailException(HttpServletRequest request, HttpServletResponse response) {
        log.error("컨드롤러의 오류를 낚아 채왔다");

        log.info("request에 담겨있는 유효하지 않은 access 와 refresh 모두 제거한다.");
        Cookie[] cookies = request.getCookies();
        expireCookie(response, authCookieName);
        expireCookie(response, refreshCookieName);
        return "index";

    }

    private void expireCookie(HttpServletResponse response, String cookieName) {
        Cookie cookie = new Cookie(cookieName, null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }
}
