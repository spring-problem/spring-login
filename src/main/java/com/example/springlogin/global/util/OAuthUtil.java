package com.example.springlogin.global.util;

import com.example.springlogin.domain.member.controller.request.OAuthRequest;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class OAuthUtil {
    public static String makeUrlForLogin(String clientId, String redirectUri) {
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
        return send;
    }

    public static String makeUrlForToken(OAuthRequest oAuthRequest, String clientId, String clientSecret) throws UnsupportedEncodingException {
        String url = "https://nid.naver.com/oauth2.0/token";

        // URL에 파라미터 추가
        String sendUrl = String.format("%s?grant_type=authorization_code&client_id=%s&client_secret=%s&code=%s&state=%s",
                url, clientId, clientSecret,  oAuthRequest.getCode(), URLEncoder.encode("1234", "UTF-8"));
        return sendUrl;
    }

}
