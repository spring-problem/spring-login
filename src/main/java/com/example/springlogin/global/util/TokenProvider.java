package com.example.springlogin.global.util;

import com.example.springlogin.member.domain.Member;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class TokenProvider {
    private String secret;
    private long tokenValidTime;

    @Autowired
    public TokenProvider(@Value("${jwt.secret}") String secret, @Value("${jwt.tokenValidTime}") long tokenValidTime) {
        this.secret = Base64.getEncoder().encodeToString(secret.getBytes());
        this.tokenValidTime = tokenValidTime;
    }
    private Map<String, Object> map;


    public String generateToken(Member member) {
        createHeader();
        return Jwts.builder()
                .setHeader(map)
                .setClaims(createClaims(member.getId()))
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    Claims createClaims(Long id) {
        Claims claims = Jwts.claims()
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + tokenValidTime));
        claims.put("userID", id);
        return claims;
    }

    void createHeader() {
        String alg = "HS256";
        String typ = "JWT";
        map = new HashMap<>();
        map.put("alg" , alg);
        map.put("typ" , typ);
    }
}
