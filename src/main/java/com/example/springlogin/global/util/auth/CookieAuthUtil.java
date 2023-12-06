package com.example.springlogin.global.util.auth;

import com.example.springlogin.member.domain.Member;
import com.example.springlogin.member.service.MemberService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

import java.util.Optional;


@RequiredArgsConstructor
public class CookieAuthUtil implements AuthUtil{
    private final String loginCookieName = "userId";

    @Override
    public Long getMemberId(HttpServletRequest request) {
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
