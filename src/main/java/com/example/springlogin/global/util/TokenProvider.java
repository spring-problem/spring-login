package com.example.springlogin.global.util;

import com.example.springlogin.config.JwtConfig;
import com.example.springlogin.member.domain.Member;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TokenProvider {
    private String screct;
    private long tokenValidTime;
    public TokenProvider() {
        this.screct = Base64.getEncoder().encodeToString(JwtConfig.getSecret().getBytes());
        this.tokenValidTime = JwtConfig.getTokenValidTime();
    }
    private Map<String, Object> map;
    private Claims claims;
    private String token;

    public String generateToken(Member member) {
        createClaims(member.getId());
        createHeader();
        token = Jwts.builder()
                .setHeader(map)
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS256, screct)
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
