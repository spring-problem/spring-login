package com.example.springlogin.global.util;

import com.example.springlogin.member.domain.Member;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class TokenProvider {
    private final String alg = "HS256";
    private final String typ = "JWT";
    private String secret;
    private long expireTime;

    TokenProvider(@Value("${jwt.secret}") String secret,
                  @Value("${jwt.token-validity-in-seconds}") long expireTime){
        this.secret = Base64.getEncoder().encodeToString(secret.getBytes());
        this.expireTime = expireTime;

    }

    public String generateToken(Member member) {
        String token = Jwts.builder()
                .setHeader(createHeader())
                .setClaims(createClaims(member.getId()))
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();

        return token;
    }

    Claims createClaims(Long id) {
        Claims claims = Jwts.claims()
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expireTime));
        claims.put("userID", id);
        return claims;
    }

    Map<String, Object> createHeader() {
        Map<String, Object> map = new HashMap<>();
        map.put("alg" , alg);
        map.put("typ" , typ);
        return map;
    }
}
