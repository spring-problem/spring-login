package com.example.springlogin.auth.service;

import com.example.springlogin.auth.domain.RefreshToken;
import com.example.springlogin.auth.repository.AuthRepository;
import com.example.springlogin.auth.service.param.GenerateRefreshTokenParam;
import com.example.springlogin.member.domain.Member;
import com.example.springlogin.member.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
@Slf4j
public class AuthService {

    AuthRepository authRepository;
    MemberRepository memberRepository;

    @Transactional(readOnly = false)
    public void generateRefreshToken(GenerateRefreshTokenParam param) {
        Optional<Member> member = memberRepository.findById(param.getMemberId());
        RefreshToken refreshToken = RefreshToken.createRefreshToken(member.get(), param.getToken());
        authRepository.save(refreshToken);
        log.info("token 등록 완료");
    }
}
