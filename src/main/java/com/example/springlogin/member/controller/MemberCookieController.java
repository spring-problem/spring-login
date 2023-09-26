package com.example.springlogin.member.controller;

import com.example.springlogin.member.controller.request.LoginRequest;
import com.example.springlogin.member.domain.Member;
import com.example.springlogin.member.param.LoginParam;
import com.example.springlogin.member.service.MemberService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;


@RequiredArgsConstructor
public class MemberCookieController implements MemberController {

    private final MemberService service;

//    public MemberCookieController(MemberService service){
//        this.service = service;
//    } =>@RequiredArgsConstructor 로 대체 가능

    @Override
    public String getLoginPage(HttpServletRequest request, HttpServletResponse response, Model model) {

        model.addAttribute("loginRequest", new LoginRequest());
        Cookie[] cookies = request.getCookies();
        if(cookies != null){
            for(Cookie cookie : cookies) {
                if(cookie.getName().equals("loginByCookie")){
                    return "/";
                }
            }
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
            Cookie cookie = new Cookie("loginByCookie", "true");
            cookie.setMaxAge(60*60*24*400);
            cookie.setPath(request.getContextPath());
            response.addCookie(cookie);
            return "redirect:/";
        }
        return "redirect:/login";
    }

    @Override
    public String logout(HttpServletRequest request, HttpServletResponse response, Model model) {
        Cookie cookie = new Cookie("loginByCookie", "false");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return "redirect:/";
    }
}
