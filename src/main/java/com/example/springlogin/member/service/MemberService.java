package com.example.springlogin.member.service;

import com.example.springlogin.member.domain.Member;

import java.util.Optional;


public interface MemberService {
    void join(JoinParam param);
    Optional<Member> login(LoginParam loginRequest);

    Optional<Member> getLoginUserById(Long id);
}
