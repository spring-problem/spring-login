package com.example.springlogin.global.util;

import com.example.springlogin.member.domain.Member;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class TokenProvider {
    private final SignatureAlgorithm alg = SignatureAlgorithm.valueOf("HS256");
    private final String typ = "JWT";
    private final SecretKey secret;
    private final long expireTimeMilliSecond;

    public TokenProvider(@Value("${jwt.secret}") String secret,
                         @Value("${jwt.token-validity-in-seconds}") String expireTime){
        this.secret = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
        this.expireTimeMilliSecond = Long.parseLong(expireTime) * 1000;
    }

    public String generateToken(Member member) {
        String token = Jwts.builder()
                .setHeader(createHeader())
                .setClaims(createClaims(member.getId()))
                .signWith(secret, SignatureAlgorithm.HS256)
                .compact();
        return token;
    }

    Claims createClaims(Long id) {
        Claims claims = Jwts.claims()
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expireTimeMilliSecond))
                .setSubject(String.valueOf(id));
        return claims;
    }

    Map<String, Object> createHeader() {
        Map<String, Object> map = new HashMap<>();
        map.put("alg" , alg);
        map.put("typ" , typ);
        return map;
    }
}
