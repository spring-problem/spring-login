package com.example.springlogin.member.controller;

import com.example.springlogin.member.domain.Member;
import com.example.springlogin.member.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.Optional;

@RequiredArgsConstructor
public class MemberSessionController implements MemberController {

    private final MemberService memberService;
    @Override
    public String getLoginPage(HttpServletRequest request, HttpServletResponse response, Model model) {
        HttpSession session = request.getSession();

        // 이미 로그인이 되어있는 경우
        if (session != null && session.getAttribute("loginBySession") != null){
            // 현재 getHomePage가 쿠키로 설정이 되어있어서 애매하다 !
            return "redirect:/";
        }

        return "/login";
    }

    @Override
    public String login(@ModelAttribute LoginRequest loginRequest, HttpServletRequest request, HttpServletResponse response, Model model) {
        LoginParam loginParam = LoginParam.builder()
                .email(loginRequest.getEmail())
                .password(loginRequest.getPassword())
                .build();

        Optional<Member> member = memberService.login(loginParam);
        // 로그인에 성공했다면 세션을 생성한다.
        if(member.isPresent()) {
            // 이 과정에서 ( 세션을 생성하는 ) API에서 자동으로 랜덤 키를 부여해준다 (브라우저에 쿠키로 키값을 부여한다)
            HttpSession session = request.getSession();
            // 서버의 세션 저장 공간에 해당 이름(쿠키로 부여받은 키값) 으로 열수있는 객체를 넣어둔다
            session.setAttribute("loginBySession", member.get());
            session.setMaxInactiveInterval(60*30);
            return "redirect:/";
        }
        return "redirect:/login";
    }

    @Override
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        // 한 브라우저에는 하나의 세션만 존재가 가능하기에 -> 그냥 이름 없이 가져올수 있는건가?
        HttpSession session = request.getSession();
        session.invalidate();
        return "redirect:/";
    }
}
