package com.example.springlogin.member.controller.response;

import com.example.springlogin.member.domain.Member;
import com.example.springlogin.member.domain.Role;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class MembersResponse {
    private Long id;
    private String email;
    private String password;
    private Role role;

    public static MembersResponse changeToResponse(Member member) {
        return MembersResponse.builder()
                .id(member.getId())
                .email(member.getEmail())
                .password(member.getPassword())
                .role(member.getRole())
                .build();
    }
}
