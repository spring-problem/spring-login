package com.example.springlogin.member.controller;

import com.example.springlogin.member.controller.request.JoinRequest;
import com.example.springlogin.member.controller.request.LoginRequest;
import com.example.springlogin.member.domain.Member;
import com.example.springlogin.member.service.MemberService;
import com.example.springlogin.member.service.param.JoinParam;
import com.example.springlogin.member.service.param.LoginParam;
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
    public String getHomepage(HttpServletRequest request, HttpServletResponse response, Model model) {
        HttpSession session = request.getSession(false);

        if(session != null){
            String id = (String) session.getAttribute("loginBySession");
            if(id != null){
                Optional<Member> user = service.getLoginUserById(Long.parseLong(id));
                model.addAttribute("email", user.get().getEmail().toString());
            }
        }
        return "index";
    }

    @Override
    public String getLoginPage(HttpServletRequest request, HttpServletResponse response, Model model) {
        HttpSession session = request.getSession();
        if(session.getAttribute("loginBySession") != null){
            return "redirect:/";
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
            session.setAttribute("loginBySession", member.get().getId().toString());
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

    @Override
    public String getJoinPage(HttpServletRequest request, HttpServletResponse response, Model model) {
        model.addAttribute("joinRequest", JoinRequest.builder().build());
        HttpSession session = request.getSession();
        if(session.getAttribute("loginBySession") != null){
            return "redirect:/";
        }
        return "join";
    }

    @Override
    public String join(@ModelAttribute JoinRequest joinRequest, HttpServletRequest request, HttpServletResponse response) {
        JoinParam param = JoinParam.builder()
                .email(joinRequest.getEmail())
                .password(joinRequest.getPassword())
                .build();

        service.join(param);
        return "redirect:/";
    }
}
