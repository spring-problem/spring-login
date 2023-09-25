package com.example.springlogin.member.service;

import com.example.springlogin.member.domain.Member;


public interface MemberService {
    void join(JoinParam param);
    Member login(LoginParam loginRequest);
}
