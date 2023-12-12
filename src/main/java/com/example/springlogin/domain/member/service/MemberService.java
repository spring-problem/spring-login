package com.example.springlogin.domain.member.service;

import com.example.springlogin.domain.member.domain.Member;
import com.example.springlogin.domain.member.service.param.JoinParam;
import com.example.springlogin.domain.member.service.param.LoginParam;

import java.util.List;
import java.util.Optional;


public interface MemberService {
    void join(JoinParam param);
    Optional<Member> login(LoginParam loginRequest);

    Optional<Member> getLoginUserById(Long id);

    List<Member> getAllMembers();

}
