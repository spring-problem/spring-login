package com.example.springlogin.global.util;

import com.example.springlogin.member.domain.Member;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.yaml.snakeyaml.tokens.Token.ID.Key;

@Component
public class TokenProvider {
    final private long tokenValidTime;
    private SecretKey secretKey;

    // 키 길이로 인해 나오는 오류나 일정 키를 사용하지 않고 시스템에서 랜덤키를 배부해준다 ! -> 강력하다
    //final private SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    @Autowired
    public TokenProvider(@Value("${jwt.secret}") String secret,
                         @Value("${jwt.tokenValidSecond}") long tokenValidSecond) {
        String encodedkey = Base64.getEncoder().encodeToString(secret.getBytes());
        this.secretKey = Keys.hmacShaKeyFor(encodedkey.getBytes());
        this.tokenValidTime = tokenValidSecond * 1000;
    }



    public String generateToken(Member member) {
        createHeader();
        return Jwts.builder()
                .setClaims(createClaims(member.getId()))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    Claims createClaims(Long id) {
        Claims claims = Jwts.claims()
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + tokenValidTime));
        claims.put("userID", id);
        return claims;
    }

    Map<String, Object> createHeader() {
        String alg =  "HS256";
        String typ =  "JWT";
        Map<String, Object> map = new HashMap<>();
        map.put("alg" , alg);
        map.put("typ" , typ);

        return map;
    }
}
