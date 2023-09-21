package com.example.springlogin.member.service;

import com.example.springlogin.member.domain.Member;
import com.example.springlogin.member.param.JoinParam;
import com.example.springlogin.member.request.JoinRequest;
import com.example.springlogin.member.request.LoginRequest;

public interface MemberService {
    void join(JoinParam param);
    Member login(LoginRequest loginRequest);
}
