package com.example.springlogin.config.auth;

import com.example.springlogin.domain.auth.service.AuthService;
import com.example.springlogin.domain.member.controller.MemberController;
import com.example.springlogin.domain.member.controller.MemberJwtController;
import com.example.springlogin.domain.member.service.MemberService;
import com.example.springlogin.global.exception.handler.AuthExceptionHandler;
import com.example.springlogin.global.exception.handler.JwtAuthExceptionHandler;
import com.example.springlogin.global.filter.AuthFilter;
import com.example.springlogin.global.util.TokenProvider;
import com.example.springlogin.global.util.auth.AuthUtil;
import com.example.springlogin.global.util.auth.JwtAuthUtil;
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
            AuthService authService,
            TokenProvider tokenProvider,
            @Value("${auth.jwt.access-cookie-key}") String accessCookieName,
            @Value("${auth.jwt.refresh-cookie-key}") String refreshCookieName
    ) {
        return new MemberJwtController(memberService,
                authService,
                tokenProvider,
                accessCookieName,
                refreshCookieName
        );
    }

    @Bean
    TokenProvider tokenProvider(@Value("${auth.jwt.secret}") String secret,
                                @Value("${auth.jwt.access-token-validity-in-seconds}") String accessTokenExpireTime,
                                @Value("${auth.jwt.refresh-token-validity-in-days}") String refreshTokenExpireTime
    ) {
        return new TokenProvider(
                secret,
                accessTokenExpireTime,
                refreshTokenExpireTime
        );
    }

    @Bean
    JwtAuthUtil authUtil(
            @Value("${auth.jwt.access-cookie-key}") String accessCookieName,
            @Value("${auth.jwt.refresh-cookie-key}") String refreshCookieName,
            TokenProvider tokenProvider,
            AuthService authService
    ) {
        return new JwtAuthUtil(
                accessCookieName,
                tokenProvider,
                refreshCookieName,
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

    @Bean
    AuthExceptionHandler authExceptionHandler(
            @Value("${auth.jwt.access-cookie-key}") String accessCookieName,
            @Value("${auth.jwt.refresh-cookie-key}") String refreshCookieName
    ) {
        return new JwtAuthExceptionHandler(
                accessCookieName,
                refreshCookieName
        );
    }

}
