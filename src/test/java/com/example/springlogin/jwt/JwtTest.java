package com.example.springlogin.jwt;

import com.example.springlogin.global.util.TokenProvider;
import com.example.springlogin.member.domain.Member;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest
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
        map.put("alg" , alg);
        map.put("typ" , typ);

        return map;
    }

    @Test
    public void 토큰_payload값_가져오기(){
        Member member = new Member("test@naver.com", "password");
        ReflectionTestUtils.setField(member, "id", 1L);
        String token = provider.generateToken(member);

        String secret =  "dGhpc0lzTXlTZWNyZXRLZXlNYWRlQnlKdWh5dW5Tb24=";

        String result = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().getSubject();

        // 설정한 PK 값 가져온다 !
        System.out.println(result);
    }

    @Test
    public void 토큰_검증() {
        // 클라이언트가 보내온 토큰값 !
        Member member = new Member("test@naver.com", "password");
        ReflectionTestUtils.setField(member, "id", 1L);
        String token = provider.generateToken(member);

        String secret =  "dGhpc0lzTXlTZWNyZXRLZXlNYWRlQnlKdWh5dW5Tb24=";

        Jws<Claims> claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
        System.out.println(!claims.getBody().getExpiration().before(new Date()));
    }

}
