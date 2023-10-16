package com.example.springlogin.member.controller;

import com.example.springlogin.global.util.TokenProvider;
import com.example.springlogin.member.controller.request.JoinRequest;
import com.example.springlogin.member.controller.request.LoginRequest;
import com.example.springlogin.member.domain.Member;
import com.example.springlogin.member.service.MemberService;
import com.example.springlogin.member.service.param.JoinParam;
import com.example.springlogin.member.service.param.LoginParam;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;

import java.util.Optional;

@RequiredArgsConstructor
public class MemberJwtController implements MemberController {

    private final MemberService memberService;

    private final TokenProvider tokenProvider;

    // 인터셉터 구현 어떠한 요청이 들어오더라도 토큰을 확인한후 유효할때만 권한을 준다 !
    // 만약 유효하지 않는다면 ? -> 로그아웃 시켜버리고 index 페이지로 이동시킨다
    // 현재 쿠키 부분을 JWT 확인 부분으로 교체한다

    @Override
    public String getHomepage(HttpServletRequest request, HttpServletResponse response, Model model) {
        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("Token".equals(cookie.getName())) {

                }
            }
        }

        return "index";
    }

    @Override
    public String getLoginPage(HttpServletRequest request, HttpServletResponse response, Model model) {
        model.addAttribute(LoginRequest.builder().build());
        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("Token".equals(cookie.getName())) {
                    return "redirect:/";
                }
            }
        }
        return "/login";
    }

    @Override
    public String login(LoginRequest loginRequest, HttpServletRequest request, HttpServletResponse response, Model model) {
        request.getParameter("loginRequest");
        LoginParam param = LoginParam.builder()
                .email(loginRequest.getEmail())
                .password(loginRequest.getPassword())
                .build();

        Optional<Member> member = memberService.login(param);
        if (member.isPresent()) {
            Cookie cookie = new Cookie("Token", tokenProvider.generateToken(member.get()));
            response.addCookie(cookie);
            return "redirect:/";
        }
        return "/login";
    }

    @Override
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        return "redirect:/"; // 토큰을 무효화 시킨다 ? ! or 쿠키 로컬 스토리지 값을 지운다 !? 어차피 만료 시간은 정해져 있으니 ?
    }

    @Override
    public String getJoinPage(HttpServletRequest request, HttpServletResponse response, Model model) {
//        if () { // Local storage에 토큰이 없다면 !
//            return "redirect:/";
//        }
        return "/join";
    }

    @Override
    public String join(JoinRequest joinRequest, HttpServletRequest request, HttpServletResponse response) {
        JoinParam param = JoinParam.builder()
                .email(joinRequest.getEmail())
                .password(joinRequest.getPassword())
                .build();

        memberService.join(param);
        return "redirect:/";
    }
}
