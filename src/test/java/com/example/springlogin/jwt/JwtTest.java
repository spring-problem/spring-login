package com.example.springlogin.jwt;

import com.example.springlogin.domain.member.domain.Member;
import com.example.springlogin.global.exception.JwtParsingFailException;
import com.example.springlogin.global.util.TokenProvider;
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
        Member member = Member.createMember("test@naver.com", "password");
        ReflectionTestUtils.setField(member, "id", 1L);
        System.out.println(provider.generateToken(member.getId()));
    }

    @Test
    void TokenProvider_validate_메서드_테스트() throws InterruptedException {
        //given

        Member member = Member.createMember("test@naver.com", "password");
        ReflectionTestUtils.setField(member, "id", 1L);

        //when

        String token = provider.generateToken(member.getId());

        //then

        //signature 변경
        Assertions.assertThatThrownBy(() -> provider.getClaims(token + "123"))
                .isInstanceOf(JwtParsingFailException.class);

        //토큰 만료
        Thread.sleep(1500L);
        Assertions.assertThatThrownBy(() -> provider.getClaims(token))
                .isInstanceOf(JwtParsingFailException.class);
    }

}
