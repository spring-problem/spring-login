package com.example.springlogin.global.util.auth;

import com.example.springlogin.member.domain.Member;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;

import java.util.Optional;

public class SessionAuthUtil implements AuthUtil {
    @Override
    public Long getMemberId(HttpServletRequest request) {
        HttpSession session = request.getSession();

        if (session == null) throw new RuntimeException("유저가 존재하지 않습니다.");

        String id = (String) session.getAttribute("loginBySession");
        if (id == null) throw new RuntimeException("유저가 존재하지 않습니다.");

        return Long.parseLong(id);
    }
}
