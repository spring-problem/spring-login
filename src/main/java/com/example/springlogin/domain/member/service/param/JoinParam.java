package com.example.springlogin.domain.member.service.param;


import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class JoinParam {
    private String email;
    private String password;
}
