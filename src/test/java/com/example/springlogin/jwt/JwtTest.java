package com.example.springlogin.jwt;

import com.example.springlogin.member.domain.Member;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Base64;
import java.util.Date;

@SpringBootTest
public class JwtTest {

    @Test
    void jwt_토큰_생성() {
        Member member = new Member("test@naver.com", "password");
        ReflectionTestUtils.setField(member, "id", 1L);

        String secret = Base64.getEncoder().encodeToString("asdfasdfdasf".getBytes());
        String token = Jwts.builder()
                .setClaims(createClaims(member.getId()))
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();

        System.out.println("token = " + token);
    }

    Claims createClaims(Long id) {
        Claims claims = Jwts.claims()
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 60 * 1000));
        claims.put("userID", id);
        return claims;
    }

}
