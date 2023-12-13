package com.example.springlogin.config.auth;

import com.example.springlogin.domain.member.controller.MemberController;
import com.example.springlogin.domain.member.controller.MemberCookieController;
import com.example.springlogin.domain.member.service.MemberService;
import com.example.springlogin.global.exception.handler.AuthExceptionHandler;
import com.example.springlogin.global.exception.handler.CookieAuthExceptionHandler;
import com.example.springlogin.global.exception.handler.JwtAuthExceptionHandler;
import com.example.springlogin.global.filter.AuthFilter;
import com.example.springlogin.global.util.auth.AuthUtil;
import com.example.springlogin.global.util.auth.CookieAuthUtil;
import jakarta.servlet.Filter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class CookieAuthConfig {
    @Bean
    MemberController memberController(MemberService memberService,
                                      @Value("${cookie.loginCookieName}") String loginCookieName) {
        return new MemberCookieController(memberService, loginCookieName);
    }

    @Bean
    CookieAuthUtil authUtil(
    ) {
        return new CookieAuthUtil();
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
    AuthExceptionHandler authExceptionHandler(@Value("${cookie.loginCookieName}") String loginCookieName) {
        return new CookieAuthExceptionHandler(loginCookieName);
    }
}
