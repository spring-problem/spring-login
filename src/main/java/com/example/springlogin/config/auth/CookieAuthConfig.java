package com.example.springlogin.config.auth;

import com.example.springlogin.global.filter.AuthFilter;
import com.example.springlogin.global.util.TokenProvider;
import com.example.springlogin.global.util.auth.AuthUtil;
import com.example.springlogin.global.util.auth.CookieAuthUtil;
import com.example.springlogin.global.util.auth.JwtAuthUtil;
import com.example.springlogin.member.controller.MemberController;
import com.example.springlogin.member.controller.MemberCookieController;
import com.example.springlogin.member.service.MemberService;
import jakarta.servlet.Filter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class CookieAuthConfig {
    @Bean
    MemberController memberController(MemberService memberService) {
        return new MemberCookieController(memberService);
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
}
