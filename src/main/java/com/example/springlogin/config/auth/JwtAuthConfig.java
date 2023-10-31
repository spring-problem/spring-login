package com.example.springlogin.config.auth;

import com.example.springlogin.global.util.TokenProvider;
import com.example.springlogin.member.controller.MemberController;
import com.example.springlogin.member.controller.MemberJwtController;
import com.example.springlogin.member.service.MemberService;
import jakarta.servlet.Filter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtAuthConfig {

    @Bean
    MemberController memberController(
            MemberService memberService,
            TokenProvider tokenProvider
    ) {
        return new MemberJwtController(memberService, tokenProvider);
    }

    @Bean
    TokenProvider tokenProvider(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.token-validity-in-seconds}") String expireTime
    ) {
        return new TokenProvider(secret, expireTime);
    }

//    @Bean
//    FilterRegistrationBean<Filter> filterRegistrationBean() {
//        FilterRegistrationBean<Filter> bean = new FilterRegistrationBean<>();
//        return bean;
//    }
    //bean.setFilter 가 필요하다 !

}
