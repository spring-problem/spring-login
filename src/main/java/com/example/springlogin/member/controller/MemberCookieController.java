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


public class MemberCookieController implements MemberController {
    @Autowired
    private MemberService service;

    @Override
    public String getIndexPage(HttpServletRequest request, HttpServletResponse response) {
        return "/index";
    }

    @Override
    public String getLoginPage(HttpServletRequest request, HttpServletResponse response, Model model) {
        model.addAttribute("loginRequest", new LoginRequest());
        if(request.getAttribute("loginByCookie") != null){
            boolean isLogin = (boolean) request.getAttribute("loginByCookie");
            if(isLogin){
                return "/index";
            }
        }
        return "/login";
    }

    @Override
    public String login(HttpServletRequest request, HttpServletResponse response, LoginRequest loginRequest) {
        request.getParameter("loginRequest");
        LoginParam param = LoginParam.builder()
                .email(loginRequest.getEmail())
                .password(loginRequest.getPassword())
                .build();
        System.out.println(param.toString());
        try{
            Member member = service.login(param);
            if(member != null){
                HttpSession session = request.getSession();
                Cookie cookie = new Cookie("loginByCookie" , "true");
                cookie.setMaxAge(60*60*24*400);
                response.addCookie(cookie);
                return "/index";
            }
        }catch (RuntimeException e){
            System.out.println(e.getMessage());
        }

        return "/login";
    }

    @Override
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        if(cookies != null){
            for (Cookie cookie:cookies) {
                if(cookie.getName().equals("loginByCookie")){
                    cookie.setMaxAge(0);
                    response.addCookie(cookie);
                    break;
                }
            }
        }
        return "/index";
    }
}
