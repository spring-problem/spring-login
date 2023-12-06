package com.example.springlogin.global.util.auth;

import com.example.springlogin.global.util.TokenProvider;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JwtAuthUtil implements AuthUtil {

    final String authCookieName;
    final TokenProvider tokenProvider;

    @Override
    public Long getMemberId(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            throw new RuntimeException("멤버 ID가 존재하지 않습니다.");
        }

        for (Cookie cookie : cookies) {
            if (!authCookieName.equals(cookie.getName())) {
                continue;
            }
            return Long.parseLong(tokenProvider.getClaims(cookie.getValue()).getBody().getSubject());
        }
        throw new RuntimeException("멤버 ID가 존재하지 않습니다.");
    }
}
