package com.example.springlogin.domain.member.controller;

import com.example.springlogin.domain.member.controller.request.JoinRequest;
import com.example.springlogin.domain.member.controller.request.LoginRequest;
import com.example.springlogin.domain.member.controller.request.OAuthRequest;
import com.example.springlogin.domain.member.service.MemberService;
import com.example.springlogin.global.util.OAuthUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;

@RequiredArgsConstructor
public class MemberOAuthController implements MemberController{
    private final MemberService memberService;
    private final String clientId;
    private final String clientSecret;
    private final String redirectUri;


    @Override
    public String getHomepage(HttpServletRequest request, HttpServletResponse response, Model model) {
        // 토큰이 있다면 naver 로 인증 요청 보내본후 결과 받기 !
        return "index";
    }

    // 로그인 버튼 클릭시 naver 로그인 화면으로 전환
    @Override
    public String getLoginPage(HttpServletRequest request, HttpServletResponse response, Model model) {
        // 네이버 로그인 화면으로 이동시킨다 https://nid.naver.com/oauth2.0/authorize?response_type=code&
        String send = OAuthUtil.makeUrlForLogin(clientId, redirectUri);
        //response.sendRedirect(send);
        // 이렇게 보내는데 dispatherServlet 아닌 실제 url 로 가는 이유? -> redirect 이기 때문이다
        return send;
    }


    // 로그인 이후에 redirect 되고 매핑되는 컨트롤러 -> code 를 이용하여 부가적인 정보들과(email, name) refreshToken access token 받아온다
    @GetMapping("outh")
    public String getResult(@ModelAttribute OAuthRequest oAuthRequest, HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
        String sendUrl = OAuthUtil.makeUrlForToken(oAuthRequest, clientId, clientSecret);

        // GET 요청을 보냄
        String responseBody = new RestTemplate().getForObject(sendUrl, String.class);

        // 여기에서 JSON 응답이 넘어온다 (accesstoken, refreshtoken)
        System.out.println(responseBody);

        String accessToken = null;
        String refreshToken = null;
        try {
            // ObjectMapper를 사용하여 JSON을 JsonNode로 매핑
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(responseBody);

            accessToken = jsonNode.get("access_token").asText();
            refreshToken = jsonNode.get("refresh_token").asText();
            String tokenType = jsonNode.get("token_type").asText();
            String expiresIn = jsonNode.get("expires_in").asText();

        } catch (Exception e) {
            e.printStackTrace();
        }

        // response 의 cookie 에 Token 을 담아서 보낸다 !
        Cookie accessCookie = new Cookie("OAuthAcessToken", accessToken);
        response.addCookie(accessCookie);

        Cookie refreshCookie = new Cookie("OAuthRefreshToken", refreshToken);
        response.addCookie(refreshCookie);

        return "redirect:/";
    }

    @Override
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        // 네이버로 로그아웃 요청 보내기 ! (토큰 만료 ! )

        return null;
    }

    @Override
    public String login(LoginRequest loginRequest, HttpServletRequest request, HttpServletResponse response, Model model) {
        return null;
    }

    @Override
    public String getJoinPage(HttpServletRequest request, HttpServletResponse response, Model model) {
        return null;
    }

    @Override
    public String join(JoinRequest joinRequest, HttpServletRequest request, HttpServletResponse response) {
        return null;
    }

    @Override
    public String getMembersPage(HttpServletRequest request, HttpServletResponse response, Model model) {
        return null;
    }
}
