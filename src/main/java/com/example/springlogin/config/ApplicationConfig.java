package com.example.springlogin.config;

import com.example.springlogin.global.util.TokenProvider;
import com.example.springlogin.member.controller.MemberController;
import com.example.springlogin.member.controller.MemberCookieController;
import com.example.springlogin.member.controller.MemberJwtController;
import com.example.springlogin.member.controller.MemberSessionController;
import com.example.springlogin.member.service.MemberService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {
    @Bean
    MemberController memberController(MemberService memberService, TokenProvider tokenProvider) {
        return new MemberJwtController(memberService , tokenProvider);
    }
}