package com.example.springlogin.config.auth;

import com.example.springlogin.domain.member.controller.MemberController;
import com.example.springlogin.domain.member.controller.MemberSessionController;
import com.example.springlogin.domain.member.service.MemberService;
import com.example.springlogin.global.filter.AuthFilter;
import com.example.springlogin.global.util.auth.AuthUtil;
import com.example.springlogin.global.util.auth.SessionAuthUtil;
import jakarta.servlet.Filter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SessionAuthConfig {
    @Bean
    MemberController memberController(MemberService memberService) {
        return new MemberSessionController(memberService);
    }

    @Bean
    SessionAuthUtil authUtil() {
        return new SessionAuthUtil();
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
