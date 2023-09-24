package com.example.springlogin.member.service;

import com.example.springlogin.member.domain.Member;
import com.example.springlogin.member.param.JoinParam;
import com.example.springlogin.member.param.LoginParam;

public interface MemberService {
    void join(JoinParam param);
    Member login(LoginParam loginRequest);
}
