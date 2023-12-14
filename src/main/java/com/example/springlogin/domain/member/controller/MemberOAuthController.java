package com.example.springlogin.domain.member.controller;

import com.example.springlogin.domain.member.controller.request.JoinRequest;
import com.example.springlogin.domain.member.controller.request.LoginRequest;
import com.example.springlogin.domain.member.service.MemberService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@RequiredArgsConstructor
public class MemberOAuthController implements MemberController{
    private final MemberService memberService;
    private final String clientId;
    private final String clientSecret;
    private final String redirectUri;

    @Override
    public String getHomepage(HttpServletRequest request, HttpServletResponse response, Model model) {


        return "index";
    }

    // 로그인 버튼 클릭시 naver 로그인 화면으로 전환
    @Override
    public String getLoginPage(HttpServletRequest request, HttpServletResponse response, Model model) {
        // 네이버 로그인 화면으로 이동시킨다 https://nid.naver.com/oauth2.0/authorize?response_type=code&
        String url = "https://nid.naver.com/oauth2.0/authorize?response_type=code";
        String client_id = "&client_id=" + clientId;
        String state = null;
        String redirect_uri = null;
        try {
            state = "&state=" + URLEncoder.encode("1234", "UTF-8");
            redirect_uri = "&redirect_uri=" + URLEncoder.encode(redirectUri, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

        String send = "redirect:" + url + client_id + state + redirect_uri;

//        response.sendRedirect(send);

        // 이렇게 보내는데 dispatherServlet 아닌 실제 url 로 가는 이유?
        return send;
    }

    // 로그인 이후에 redirect 되고 매핑되는 컨트롤러 -> code 를 이용하여 부가적인 정보들과(email, name) refreshToken access token 받아온다
    @GetMapping("outh")
    public String getResult(HttpServletRequest request, HttpServletResponse response,
                            @RequestParam("code") String code,
                            @RequestParam("state") String state,
                            @RequestParam(name = "error", required = false) String error,
                            @RequestParam(name = "error_descript",required = false) String error_descript) throws UnsupportedEncodingException {
        System.out.println("code = " + code);
        System.out.println("state = " + state);
        System.out.println("error = " + error);
        System.out.println("error_descript = " + error_descript);

        System.out.println("JSON 응답을 위한 요청 보낼거야 ~ ");
        String url = "https://nid.naver.com/oauth2.0/token";


        // URL에 파라미터 추가
        String sendUrl = String.format("%s?grant_type=authorization_code&client_id=%s&client_secret=%s&code=%s&state=%s",
                url, clientId, clientSecret, code, URLEncoder.encode("1234", "UTF-8"));

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
    public String login(LoginRequest loginRequest, HttpServletRequest request, HttpServletResponse response, Model model) {
        return null;
    }

    @Override
    public String logout(HttpServletRequest request, HttpServletResponse response) {
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
