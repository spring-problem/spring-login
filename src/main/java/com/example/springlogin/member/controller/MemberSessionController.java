package com.example.springlogin.member.controller;

import com.example.springlogin.member.domain.Member;
import com.example.springlogin.member.service.LoginParam;
import com.example.springlogin.member.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.Optional;

@RequiredArgsConstructor
public class MemberSessionController implements MemberController{
    private final MemberService service;

    @Override
    public String getLoginPage(HttpServletRequest request, HttpServletResponse response, Model model) {
        HttpSession session = request.getSession();
        if(session.getAttribute("loginBySession") != null){
            return "/";
        }
        return "/login";
    }

    @Override
    public String login(@ModelAttribute LoginRequest loginRequest, HttpServletRequest request, HttpServletResponse response, Model model) {
        request.getParameter("loginRequest");
        LoginParam param = LoginParam.builder()
                .email(loginRequest.getEmail())
                .password(loginRequest.getPassword())
                .build();

        Optional<Member> member = service.login(param);
        if(member.isPresent()){
            HttpSession session = request.getSession(true);
            session.setAttribute("loginBySession", "true");
            return "redirect:/";
        }
        return "/login";
    }

    @Override
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        if(session != null){
            session.invalidate();
        }
        return "redirect:/";
    }
}
