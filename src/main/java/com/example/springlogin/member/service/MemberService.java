package com.example.springlogin.member.service;

import com.example.springlogin.member.request.JoinRequest;
import com.example.springlogin.member.request.LoginRequest;

public interface MemberService {
    boolean join(JoinRequest joinRequest);
    boolean login(LoginRequest loginRequest);
}
