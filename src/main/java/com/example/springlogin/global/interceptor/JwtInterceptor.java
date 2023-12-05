package com.example.springlogin.global.interceptor;

import com.example.springlogin.global.util.TokenProvider;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;


public class JwtInterceptor implements HandlerInterceptor {

    private final TokenProvider tokenProvider;
    private final String authCookieName = "jwt";

    public JwtInterceptor(@Autowired TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception{
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            cookies = new Cookie[0];
        }

        for (Cookie cookie : cookies) {
            if (!authCookieName.equals(cookie.getName())) {
                continue;
            }
            String token = cookie.getValue();
            if (tokenProvider.isValidToken(token)) {
                return true;
            }
        }
        return false;
    }

}
