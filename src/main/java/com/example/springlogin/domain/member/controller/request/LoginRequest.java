package com.example.springlogin.domain.member.controller.request;

import lombok.Builder;
import lombok.Data;
@Builder
@Data
public class LoginRequest {
    private String email;
    private String password;
}
