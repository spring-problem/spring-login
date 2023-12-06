package com.example.springlogin.member.controller.response;

import com.example.springlogin.member.domain.Role;
import lombok.Builder;
import lombok.Data;

@Data
public class MembersResponse {
    private Long id;
    private String email;
    private String password;
    private Role role;
}
