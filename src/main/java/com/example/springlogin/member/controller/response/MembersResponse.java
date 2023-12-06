package com.example.springlogin.member.controller.response;

import com.example.springlogin.member.domain.Member;
import com.example.springlogin.member.domain.Role;
import lombok.Data;

@Data
public class MembersResponse {
    private Long id;
    private String email;
    private String password;
    private Role role;

    public static MembersResponse changeToResponse(Member member) {
        MembersResponse membersResponse = new MembersResponse();
        membersResponse.id = member.getId();
        membersResponse.email = member.getEmail();
        membersResponse.password = member.getPassword();
        membersResponse.role = member.getRole();
        return membersResponse;
    }
}
