package com.example.springlogin.global.util.auth;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public class CookieAuthUtil implements AuthUtil{
    private final String loginCookieName = "userId";

    @Override
    public Long getMemberId(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        String userId = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (loginCookieName.equals(cookie.getName())) {
                    userId = cookie.getValue();
                    if (userId.isEmpty()) {
                        throw new RuntimeException("유저가 존재하지 않습니다.");
                    }
                } else {
                    throw new RuntimeException("유저가 존재하지 않습니다..");
                }
            }
        }

        return Long.parseLong(userId);
    }
}
