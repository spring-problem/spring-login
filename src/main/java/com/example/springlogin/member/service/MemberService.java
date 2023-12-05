package com.example.springlogin.member.service;

import com.example.springlogin.member.domain.Member;
import com.example.springlogin.member.service.param.JoinParam;
import com.example.springlogin.member.service.param.LoginParam;

import java.util.List;
import java.util.Optional;


public interface MemberService {
    void join(JoinParam param);
    Optional<Member> login(LoginParam loginRequest);

    Optional<Member> getLoginUserById(Long id);

    List<Member> getAllMembers();

}
