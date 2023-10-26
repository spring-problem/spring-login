package com.example.springlogin.config.auth;

import com.example.springlogin.member.controller.MemberController;
import com.example.springlogin.member.controller.MemberCookieController;
import com.example.springlogin.member.service.MemberService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CookieAuthConfig {
    @Bean
    MemberController memberController(MemberService memberService) {
        return new MemberCookieController(memberService);
    }
}
