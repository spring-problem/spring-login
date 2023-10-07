package com.example.springlogin.member.controller;

import com.example.springlogin.member.controller.request.JoinRequest;
import com.example.springlogin.member.controller.request.LoginRequest;
import com.example.springlogin.member.domain.Member;
import com.example.springlogin.member.service.MemberService;
import com.example.springlogin.member.service.param.JoinParam;
import com.example.springlogin.member.service.param.LoginParam;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.Optional;

@RequiredArgsConstructor
public class MemberCookieController implements MemberController {

    private final MemberService memberService;
    private final String loginCookieName = "userId";

    @Override
    public String getHomepage(HttpServletRequest request, HttpServletResponse response, Model model) {
        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (loginCookieName.equals(cookie.getName())) {
                    model.addAttribute("loginByCookie", true);
                    String userId = cookie.getValue();
                    Optional<Member> findUser = memberService.getLoginUserById(Long.parseLong(userId));
                    if (findUser.isEmpty()) {
                        throw new RuntimeException("유저가 존재하지 않습니다.");
                    }
                    model.addAttribute("email", findUser.get().getEmail());
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
                if (loginCookieName.equals(cookie.getName())) {
                    return "redirect:/";
                }
            }
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
        if (member.isPresent()) {
            Cookie cookie = new Cookie(loginCookieName, member.get().getId().toString());
            response.addCookie(cookie);
            return "redirect:/";
        }
        return "/login";
    }

    @Override
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        Cookie cookie = new Cookie(loginCookieName, null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return "redirect:/";
    }

    @Override
    public String getJoinPage(HttpServletRequest request, HttpServletResponse response, Model model) {
        model.addAttribute(JoinRequest.builder().build());
        return "join";
    }

    @Override
    public String join(@ModelAttribute JoinRequest joinRequest, HttpServletRequest request, HttpServletResponse response) {
        JoinParam param = JoinParam.builder()
                .email(joinRequest.getEmail())
                .password(joinRequest.getPassword())
                .build();

        memberService.join(param);
        return "index";
    }
}
