package com.example.springlogin.member.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id
    @GeneratedValue
    private Long id;
    private String email;
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    public static Member createMember(String email, String password) {
        Member member = new Member();
        member.email = email;
        member.password = password;
        member.role = Role.MEMBER;
        return member;
    }

    public static Member createAdmin(String email, String password) {
        Member member = new Member();
        member.email = email;
        member.password = password;
        member.role = Role.ADMIN;
        return member;
    }

}
