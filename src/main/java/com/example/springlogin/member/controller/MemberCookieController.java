package com.example.springlogin.member.controller;

import com.example.springlogin.member.controller.request.LoginRequest;
import com.example.springlogin.member.domain.Member;
import com.example.springlogin.member.param.LoginParam;
import com.example.springlogin.member.service.MemberService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;

import java.util.Optional;


public class MemberCookieController implements MemberController {
    @Autowired
    private MemberService service;

    @Override
    public String getLoginPage(HttpServletRequest request, HttpServletResponse response, Model model, @CookieValue(value = "loginByCookie", required = false) boolean loginByCookie) {
        model.addAttribute("loginRequest", new LoginRequest());

        if(loginByCookie == true){
            return "/";
        }
        return "/login";
    }

    @Override
    public String login(HttpServletRequest request, HttpServletResponse response, LoginRequest loginRequest, Model model) {
        request.getParameter("loginRequest");
        LoginParam param = LoginParam.builder()
                .email(loginRequest.getEmail())
                .password(loginRequest.getPassword())
                .build();


        Member member = service.login(param);
        if(member != null){
            HttpSession session = request.getSession();
            session.setAttribute("loginByCookie", loginRequest.getEmail());
            Cookie cookie = new Cookie("loginByCookie", "true");
            cookie.setMaxAge(60*60*24*400);
            cookie.setPath(request.getContextPath());
            response.addCookie(cookie);
            return "redirect:/";
        }

        return "/login";
    }

    @Override
    public String logout(HttpServletRequest request, HttpServletResponse response, Model model) {
        Cookie cookie = new Cookie("loginByCookie", "false");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        HttpSession session = request.getSession();
        session.invalidate();
        return "redirect:/";
    }
}
