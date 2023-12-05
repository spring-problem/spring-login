package com.example.springlogin.config;

import com.example.springlogin.config.auth.JwtAuthConfig;
import com.example.springlogin.member.domain.Member;
import com.example.springlogin.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.event.EventListener;
import org.springframework.transaction.annotation.Transactional;

@Configuration
@Import(JwtAuthConfig.class)
@RequiredArgsConstructor
public class ApplicationConfig {

    final MemberRepository memberRepository;

    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    public void init() {
        Member admin = Member.createAdmin("admin@a.com", "admin");
        memberRepository.save(admin);
    }
}