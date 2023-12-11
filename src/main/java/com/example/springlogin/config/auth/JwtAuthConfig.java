package com.example.springlogin.config.auth;

import com.example.springlogin.auth.service.AuthService;
import com.example.springlogin.global.filter.AuthFilter;
import com.example.springlogin.global.util.TokenProvider;
import com.example.springlogin.global.util.auth.AuthUtil;
import com.example.springlogin.global.util.auth.JwtAuthUtil;
import com.example.springlogin.member.controller.MemberController;
import com.example.springlogin.member.controller.MemberJwtController;
import com.example.springlogin.member.service.MemberService;
import jakarta.servlet.Filter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class JwtAuthConfig {

    @Bean
    MemberController memberController(
            MemberService memberService,
            TokenProvider tokenProvider,
            @Value("${auth.jwt.access-cookie-key}") String authCookieName
    ) {
        return new MemberJwtController(memberService,
                tokenProvider,
                authCookieName,
                "refresh-jwt"
        );
    }

    @Bean
    TokenProvider tokenProvider(@Value("${jwt.secret}") String secret,
                                @Value("${jwt.token-validity-in-seconds}") String expireTime
    ) {
        return new TokenProvider(secret, expireTime);
    }

    @Bean
    JwtAuthUtil authUtil(
            @Value("${auth.jwt.access-cookie-key}") String authCookieName,
            TokenProvider tokenProvider,
            AuthService authService
    ) {
        return new JwtAuthUtil(
                authCookieName,
                tokenProvider,
                "refresh-jwt",
                authService
        );
    }

    @Bean
    FilterRegistrationBean<Filter> authFilter(
            MemberService memberService,
            AuthUtil authUtil
    ) {
        FilterRegistrationBean<Filter> bean = new FilterRegistrationBean<>();

        AuthFilter filter = new AuthFilter(
                authUtil,
                memberService
        );

        bean.setFilter(filter);
        bean.setUrlPatterns(List.of("/members"));
        return bean;
    }

}
