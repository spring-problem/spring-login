package com.example.springlogin.config.auth;

import com.example.springlogin.domain.member.controller.MemberController;
import com.example.springlogin.domain.member.controller.MemberOAuthController;
import com.example.springlogin.domain.member.service.MemberService;
import com.example.springlogin.global.exception.handler.AuthExceptionHandler;
import com.example.springlogin.global.exception.handler.CookieAuthExceptionHandler;
import com.example.springlogin.global.exception.handler.OAuthExceptionHanlder;
import com.example.springlogin.global.util.OAuthUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OAuthAuthConfig {
    @Bean
    MemberController memberController(MemberService memberService,
                                      @Value("${auth.naver.clientId}") String clientId,
                                      @Value("${auth.naver.clientSecret}") String clientSecret,
                                      @Value("${auth.naver.redirectUri}") String redirectUri) {
        return new MemberOAuthController(memberService, clientId, clientSecret, redirectUri);
    }

    @Bean
    AuthExceptionHandler authExceptionHandler() {
        return new OAuthExceptionHanlder();
    }
}
