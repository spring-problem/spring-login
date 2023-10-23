package com.example.springlogin.member.controller;

import com.example.springlogin.global.util.TokenProvider;
import com.example.springlogin.member.controller.request.JoinRequest;
import com.example.springlogin.member.controller.request.LoginRequest;
import com.example.springlogin.member.domain.Member;
import com.example.springlogin.member.service.MemberService;
import com.example.springlogin.member.service.param.JoinParam;
import com.example.springlogin.member.service.param.LoginParam;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;

import java.util.Optional;

@RequiredArgsConstructor
public class MemberJwtController implements MemberController {

    private final MemberService memberService;
    private final TokenProvider tokenProvider;

    private final String authCookieName = "jwt";

    @Override
    public String getHomepage(HttpServletRequest request, HttpServletResponse response, Model model) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            cookies = new Cookie[0];
        }

        for (Cookie cookie : cookies) {
            if (!authCookieName.equals(cookie.getName())) {
                continue;
            }
            model.addAttribute("loginByJwt", true);
            String token = cookie.getValue();

            Jws<Claims> claims = tokenProvider.getClaims(token);
            String userId = claims.getBody().getSubject();
            Optional<Member> loginUser = memberService.getLoginUserById(Long.valueOf(userId));
            if (loginUser.isEmpty()) {
                break;
            }
            model.addAttribute("email", loginUser.get().getEmail());
        }
        return "index";
    }

    @Override
    public String getLoginPage(HttpServletRequest request, HttpServletResponse response, Model model) {
        LoginRequest loginRequest = LoginRequest.builder().build();
        model.addAttribute(loginRequest);
        return "login";
    }

    @Override
    public String login(LoginRequest loginRequest, HttpServletRequest request, HttpServletResponse response, Model model) {
        LoginParam loginParam = LoginParam.builder()
                .email(loginRequest.getEmail())
                .password(loginRequest.getPassword())
                .build();

        Optional<Member> member = memberService.login(loginParam);

        if (member.isEmpty()) {
            return "login";
        }

        Member loginMember = member.get();

        String token = tokenProvider.generateToken(loginMember);
        Cookie cookie = new Cookie(authCookieName, token);
        response.addCookie(cookie);

        return "redirect:/";
    }

    @Override
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        Cookie cookie = new Cookie(authCookieName, null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return "redirect:/";
    }

    @Override
    public String getJoinPage(HttpServletRequest request, HttpServletResponse response, Model model) {
        JoinRequest joinRequest = JoinRequest.builder().build();
        model.addAttribute(joinRequest);
        return "join";
    }

    @Override
    public String join(JoinRequest joinRequest, HttpServletRequest request, HttpServletResponse response) {
        JoinParam joinParam = JoinParam.builder()
                .email(joinRequest.getEmail())
                .password(joinRequest.getPassword())
                .build();

        memberService.join(joinParam);

        return "redirect:/";
    }
}
