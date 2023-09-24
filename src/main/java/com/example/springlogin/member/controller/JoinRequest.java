package com.example.springlogin.member.controller;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class JoinRequest {
    private String email;
    private String password;
}
