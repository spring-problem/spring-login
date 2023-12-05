package com.example.springlogin.member.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id
    @GeneratedValue
    private Long id;
    private String email;
    private String password;

    private Role role;

    public Member(String email, String password) {
        this.email = email;
        this.password = password;
    }

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


    public String getEmail() {
        return email;
    }

    public Long getId() {
        return id;
    }
}
