package com.example.springlogin.member.controller;

import com.example.springlogin.member.domain.Member;
import com.example.springlogin.member.service.LoginParam;
import com.example.springlogin.member.service.MemberService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Component
public class MemberCookieController implements MemberController {

    @Autowired
    private MemberService memberService;

    @GetMapping("/")
    public String getHome() {
        return "index";
    }
    @Override
    @GetMapping("login")
    public String getLoginPage(HttpServletRequest request, HttpServletResponse response, Model model) {
        return "login";
    }

    @Override
    @PostMapping("login")
    public String login(@ModelAttribute LoginRequest loginRequest, HttpServletRequest request, HttpServletResponse response, Model model) {
        // service에 보낼 객체를 만들어서 보내준다
        LoginParam loginParam = LoginParam.builder()
                .email(loginRequest.getEmail())
                .password(loginRequest.getPassword())
                .build();

        Member member = memberService.login(loginParam);
        // 로그인에 성공 했다면 !
        if (member != null) {
            Cookie cookie = new Cookie("email", member.getEmail());
            // 쿠키 시간 디폴트로 설정
            response.addCookie(cookie);
        }

        model.addAttribute("email", member.getEmail());


        return "index";
    }

    @Override
    @PostMapping("logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        // 쿠키 시간을 만료시켜서 쿠키를 제거한다
        Cookie cookie = new Cookie("email", null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);

        return "index";
    }
}
