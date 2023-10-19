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
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.Model;

import java.util.Date;
import java.util.Optional;

@RequiredArgsConstructor
public class MemberJwtController implements MemberController {

    private final MemberService memberService;

    private final TokenProvider tokenProvider;

    @Value("${jwt.secret}")
    private String secret;

    // 인터셉터 구현 어떠한 요청이 들어오더라도 토큰을 확인한후 유효할때만 권한을 준다 !
    // 만약 유효하지 않는다면 ? -> 로그아웃 시켜버리고 index 페이지로 이동시킨다
    // 현재 쿠키 부분을 JWT 확인 부분으로 교체한다

    @Override
    public String getHomepage(HttpServletRequest request, HttpServletResponse response, Model model) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if(cookie.getName().equals("JwtToken")) {
                    model.addAttribute("loginByJwt", true);
                    String token = cookie.getValue();
                    System.out.println("토큰 = " + token);
                    if (token == "") {
                        return "index";
                    }
                    // 토큰 payLoad 값 가져오기
                    String userId = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().getSubject();

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
        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                // 토큰이 존재한다면
                if ("JwtToken".equals(cookie.getName())) {
                    String token = cookie.getValue();
                    if (token == "") {
                        return "index";
                    }
                    // 해당 토큰이 유효하다면 ( 토큰 검증 과정 )
                    Jws<Claims> claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
                    if (!claims.getBody().getExpiration().before(new Date())) {
                        return "redirect:/";
                    }
                }
            }
        }
        model.addAttribute(LoginRequest.builder().build());
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
            Cookie cookie = new Cookie("JwtToken", tokenProvider.generateToken(member.get()));
            response.addCookie(cookie);
            return "redirect:/";
        }
        return "/login";
    }

    @Override
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        // 토큰의 jwt 토큰값을 없앤다
        Cookie cookie = new Cookie("JwtToken", null);
        response.addCookie(cookie);
        return "redirect:/"; // 토큰을 무효화 시킨다 ? ! or 쿠키 로컬 스토리지 값을 지운다 !? 어차피 만료 시간은 정해져 있으니 ?
    }

    @Override
    public String getJoinPage(HttpServletRequest request, HttpServletResponse response, Model model) {
        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                // 토큰이 존재한다면
                if ("JwtToken".equals(cookie.getName())) {
                    String token = cookie.getValue();
                    if (token == "") {
                        return "index";
                    }
                    // 해당 토큰이 유효하다면 ( 토큰 검증 과정 )
                    Jws<Claims> claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
                    if (!claims.getBody().getExpiration().before(new Date())) {
                        return "redirect:/";
                    }
                }
            }
        }

        model.addAttribute(JoinRequest.builder().build());
        return "join";
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
