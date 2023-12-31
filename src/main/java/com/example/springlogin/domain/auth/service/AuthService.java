package com.example.springlogin.domain.auth.service;

import com.example.springlogin.domain.auth.domain.RefreshToken;
import com.example.springlogin.domain.auth.repository.AuthRepository;
import com.example.springlogin.domain.auth.service.param.GenerateRefreshTokenParam;
import com.example.springlogin.domain.member.domain.Member;
import com.example.springlogin.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
@Slf4j
@RequiredArgsConstructor
public class AuthService {

    final AuthRepository authRepository;
    final MemberRepository memberRepository;


    @Transactional(readOnly = false)
    public void generateRefreshToken(GenerateRefreshTokenParam param) {
        authRepository.deleteMemberToken(param.getMemberId());
        Optional<Member> member = memberRepository.findById(param.getMemberId());
        RefreshToken refreshToken = RefreshToken.createRefreshToken(member.get(), param.getToken());
        authRepository.save(refreshToken);
        log.info("token 등록 완료");
    }

    @Transactional(readOnly = false)
    public void deleteRefreshToken(GenerateRefreshTokenParam param) {
        Long memberId = param.getMemberId();
        authRepository.deleteMemberToken(memberId);

    }
    public String getRefreshToken(GenerateRefreshTokenParam param) {
        Long memberId = param.getMemberId();
        return authRepository.getMemberToken(memberId);
    }
}
