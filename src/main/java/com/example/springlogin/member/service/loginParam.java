package com.example.springlogin.member.service;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class loginParam {
    private String email;
    private String password;
}
