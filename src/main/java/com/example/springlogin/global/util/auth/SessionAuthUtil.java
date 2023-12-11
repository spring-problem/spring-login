package com.example.springlogin.global.util.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class SessionAuthUtil implements AuthUtil {
    @Override
    public Long getMemberId(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();

        if (session == null) throw new RuntimeException("유저가 존재하지 않습니다.");

        String id = (String) session.getAttribute("loginBySession");
        if (id == null) throw new RuntimeException("유저가 존재하지 않습니다.");

        return Long.parseLong(id);
    }
}
