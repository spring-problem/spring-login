package com.example.springlogin.domain.auth.domain;

import com.example.springlogin.domain.member.domain.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String token;

    @OneToOne
    private Member member;

    public static RefreshToken createRefreshToken(Member member, String token) {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.member = member;
        refreshToken.token = token;
        return refreshToken;
    }

}
