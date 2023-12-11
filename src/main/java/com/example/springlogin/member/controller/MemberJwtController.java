package com.example.springlogin.member.controller;

import com.example.springlogin.global.util.TokenProvider;
import com.example.springlogin.member.controller.request.JoinRequest;
import com.example.springlogin.member.controller.request.LoginRequest;
import com.example.springlogin.member.controller.response.MembersResponse;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class MemberJwtController implements MemberController {

    private final MemberService memberService;
    private final TokenProvider tokenProvider;

    private final String authCookieName;
    private final String refreshCookieName;

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
            String token = cookie.getValue();

            Jws<Claims> claims = tokenProvider.getClaims(token);
            String userId = claims.getBody().getSubject();
            Optional<Member> loginUser = memberService.getLoginUserById(Long.valueOf(userId));
            if (loginUser.isEmpty()) {
                break;
            }

            model.addAttribute("loginByJwt", true);
            model.addAttribute("email", loginUser.get().getEmail());
        }
        return "index";
    }

    @Override
    public String getLoginPage(HttpServletRequest request, HttpServletResponse response, Model model) {
        Optional<Cookie> authCookie = getAuthCookie(request);

        if (authCookie.isPresent()) {
            return "redirect:/";
        }

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

        String accessToken = tokenProvider.generateToken(loginMember.getId());
        Cookie accessCookie = new Cookie(authCookieName, accessToken);
        response.addCookie(accessCookie);

        String refreshToken = tokenProvider.generateRefreshToken(loginMember.getId());
        Cookie refreshCookie = new Cookie(refreshCookieName, refreshToken);
        response.addCookie(refreshCookie);

        return "redirect:/";
    }

    @Override
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        expireCookie(response, authCookieName);
        return "redirect:/";
    }

    private void expireCookie(HttpServletResponse response, String cookieName) {
        Cookie cookie = new Cookie(cookieName, null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
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

    Optional<Cookie> getAuthCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return Optional.empty();
        }

        for (Cookie cookie : cookies) {
            if (!authCookieName.equals(cookie.getName())) {
                continue;
            }
            return Optional.of(cookie);
        }
        return Optional.empty();
    }

    public String getMembersPage(HttpServletRequest request, HttpServletResponse response, Model model) {
        List<Member> list = memberService.getAllMembers();
        List<MembersResponse> members = new ArrayList<>();
        for (Member tmp : list) {
            MembersResponse membersResponse = MembersResponse.changeToResponse(tmp);
            members.add(membersResponse);
        }

        model.addAttribute("members", members);
        return "members";
    }
}
