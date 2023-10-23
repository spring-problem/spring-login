package com.example.springlogin.jwt;

import com.example.springlogin.global.exception.AuthException;
import com.example.springlogin.global.util.TokenProvider;
import com.example.springlogin.member.domain.Member;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest
@ActiveProfiles("token-provider-test")
public class JwtTest {

    private final TokenProvider provider;

    @Autowired
    public JwtTest(TokenProvider provider) {
        this.provider = provider;
    }

    @Test
    void TokenProvider_사용_토큰_생성() {
        Member member = new Member("test@naver.com", "password");
        ReflectionTestUtils.setField(member, "id", 1L);
        System.out.println(provider.generateToken(member));
    }

    @Test
    void jwt_토큰_생성() {
        Member member = new Member("test@naver.com", "password");
        ReflectionTestUtils.setField(member, "id", 1L);

        String secret = Base64.getEncoder().encodeToString("asdfasdfdasf".getBytes());
        String token = Jwts.builder()
                .setHeader(createHeader())
                .setClaims(createClaims(member.getId()))
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
        System.out.println(secret);
        System.out.println("token = " + token);
    }

    Claims createClaims(Long id) {
        Claims claims = Jwts.claims()
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 60 * 1000));
        claims.put("userID", id);
        return claims;
    }

    Map<String, Object> createHeader() {
        String alg = "HS256";
        String typ = "JWT";

        Map<String, Object> map = new HashMap<>();
        map.put("alg", alg);
        map.put("typ", typ);

        return map;
    }

    @Test
    void TokenProvider_validate_메서드_테스트() throws InterruptedException {
        //given

        Member member = new Member("test@naver.com", "password");
        ReflectionTestUtils.setField(member, "id", 1L);

        //when

        String token = provider.generateToken(member);

        //then

        //signature 변경
        Assertions.assertThatThrownBy(() -> provider.getClaims(token + "123"))
                        .isInstanceOf(AuthException.class);

        //토큰 만료
        Thread.sleep(1500L);
        Assertions.assertThatThrownBy(() -> provider.getClaims(token))
                .isInstanceOf(AuthException.class);
    }

}
