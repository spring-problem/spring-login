package com.example.springlogin.global.util;

import com.example.springlogin.member.domain.Member;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;

import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TokenProvider {
    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.tokenValidTime}")
    private long tokenValidTime;
    private Map<String, Object> map;
    private Claims claims;
    private String token;

    public String generateToken(Member member) {
        createClaims(member.getId());
        createHeader();
        token = Jwts.builder()
                .setHeader(map)
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();

        return token;
    }

    void createClaims(Long id) {
        claims = Jwts.claims()
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + tokenValidTime));
        claims.put("userID", id);
    }

    void createHeader() {
        String alg = "HS256";
        String typ = "JWT";
        map = new HashMap<>();
        map.put("alg" , alg);
        map.put("typ" , typ);
    }
}
