package com.example.springlogin.member.controller;

import com.example.springlogin.global.util.TokenProvider;
import com.example.springlogin.member.controller.request.JoinRequest;
import com.example.springlogin.member.controller.request.LoginRequest;
import com.example.springlogin.member.controller.response.MembersResponse;
import com.example.springlogin.member.domain.Member;
import com.example.springlogin.member.domain.Role;
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

        String token = tokenProvider.generateToken(loginMember);
        Cookie cookie = new Cookie(authCookieName, token);
        response.addCookie(cookie);

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
        // 1 토큰이 유효할때  아니면 ? throw 던지고
        Optional<Cookie> cookie = getAuthCookie(request);
        // 토큰이 없을때 체크
        if(cookie.isEmpty()) { throw new RuntimeException("토큰이 존재하지 않음");}

        String token = cookie.get().getValue();

        Jws<Claims> claims = tokenProvider.getClaims(token);
        Long memberId = Long.parseLong(claims.getBody().getSubject());
//        System.out.println("claims = " + claims);

//        System.out.println("token = " + token);
//        System.out.println(token.get().getValue());
        // 2 권한이 admin 일때 아니면 ?

        Optional<Member> member = memberService.getLoginUserById(memberId);
        if(member.isEmpty()) { throw new RuntimeException("해당 하는 PK 값이 존재하지 않습니다."); }

        if(!member.get().getRole().equals(Role.ADMIN)) {
            throw new RuntimeException("관리자 권한이 없습니다.");
        }

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
