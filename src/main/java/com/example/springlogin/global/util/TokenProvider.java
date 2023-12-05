package com.example.springlogin.global.util;

import com.example.springlogin.global.exception.JwtParsingFailException;
import com.example.springlogin.member.domain.Member;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TokenProvider {
    private final SignatureAlgorithm alg = SignatureAlgorithm.HS256;
    private final String typ = "JWT";
    private final SecretKey secret;
    private final long expireTimeMilliSecond;

    public TokenProvider(String secret,
                         String expireTime
    ) {
        this.secret = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
        this.expireTimeMilliSecond = Long.parseLong(expireTime) * 1000;
    }

    public String generateToken(Member member) {
        String token = Jwts.builder()
                .setHeader(createHeader())
                .setClaims(createClaims(member.getId()))
                .signWith(secret, alg)
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
        map.put("alg", alg);
        map.put("typ", typ);
        return map;
    }

    public Jws<Claims> getClaims(String token) {
        JwtParser parser = Jwts.parserBuilder()
                .setSigningKey(secret)
                .build();
        try {
            return parser.parseClaimsJws(token);
        } catch (SignatureException | ExpiredJwtException | UnsupportedJwtException | MalformedJwtException |
                 IllegalArgumentException e) {
            throw new JwtParsingFailException(e);
        }
    }

    public boolean isValidToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secret)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return !claims.getExpiration().before(new Date());
    }
}
