package com.example.springlogin.member.service.param;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginParam {
    private String email;
    private String password;
}
