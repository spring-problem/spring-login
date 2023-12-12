package com.example.springlogin.global.util;

import com.example.springlogin.global.exception.JwtParsingFailException;
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
    private final long accessTokenExpireTimeMillSecond;
    private final long refreshTokenExpireTimeMillSecond;

    public TokenProvider(String secret,
                         String accessTokenExpireTime,
                         String refreshTokenExpireTime
    ) {
        this.secret = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
        this.accessTokenExpireTimeMillSecond = Long.parseLong(accessTokenExpireTime) * 1000;
        this.refreshTokenExpireTimeMillSecond = Long.parseLong(refreshTokenExpireTime) * 24 * 60 * 60;
    }

    public String generateToken(Long id) {
        String token = Jwts.builder()
                .setHeader(createHeader())
                .setClaims(createClaims(id, accessTokenExpireTimeMillSecond))
                .signWith(secret, alg)
                .compact();
        return token;
    }

    public String generateRefreshToken(Long id) {
        String token = Jwts.builder()
                .setHeader(createHeader())
                .setClaims(createClaims(id, refreshTokenExpireTimeMillSecond))
                .signWith(secret, alg)
                .compact();
        return token;
    }

    Claims createClaims(Long id, long expireTime) {
        Claims claims = Jwts.claims()
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expireTime))
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
}
