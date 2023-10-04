package com.example.springlogin.config;

import com.example.springlogin.member.controller.MemberController;
import com.example.springlogin.member.controller.MemberCookieController;
import com.example.springlogin.member.service.MemberService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {
    @Bean
    MemberController memberCookieController(MemberService memberService) {

        return new MemberCookieController(memberService);
    }
}