package com.example.springlogin.config;

import com.example.springlogin.member.controller.MemberController;
import com.example.springlogin.member.controller.MemberCookieController;
import com.example.springlogin.member.service.MemberService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {
    @Bean
    MemberController memberCookieController(MemberService memberService) { // 빈으로 만들어 주겠다

        return new MemberCookieController(memberService); // @Bean 리턴하는 값을 스프링의 빈 컨테이너에 넣겠다
    }
}