package com.example.springlogin.global.util.auth;

import com.example.springlogin.domain.auth.service.AuthService;
import com.example.springlogin.domain.auth.service.param.GenerateRefreshTokenParam;
import com.example.springlogin.global.exception.JwtParsingFailException;
import com.example.springlogin.global.util.TokenProvider;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
public class JwtAuthUtil implements AuthUtil {

    final String accessCookieName;
    final TokenProvider tokenProvider;
    final String refreshCookieName;
    final AuthService authService;

    @Override
    public Long getMemberId(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            throw new RuntimeException("멤버 ID가 존재하지 않습니다.");
        }

        Optional<Jws<Claims>> accessToken = Optional.empty();

        // access 토큰 파싱 중 예외 발생
        try {
            accessToken = getClaimFromToken(request, accessCookieName);
        } catch (JwtParsingFailException e) {
            log.error("jwt 파싱 에러", e);

            // 가져온 토큰에 error 있는지 확인
            Optional<Jws<Claims>> refreshToken = getClaimFromToken(request, refreshCookieName);

            if (refreshToken.isEmpty()) {
                throw new RuntimeException("로그인 정보가 존재하지 않습니다.");
            }

            // db 의 refreshToken 과 cookie 와 refreshToken 과 값이 같은지 확인
            GenerateRefreshTokenParam generateRefreshTokenParam = new GenerateRefreshTokenParam();
            generateRefreshTokenParam.setMemberId(Long.valueOf(refreshToken.get().getBody().getSubject()));
            String dbToken = authService.getRefreshToken(generateRefreshTokenParam);
            // db 에서 가져온 refresh가 비어있으면 에러
            if(dbToken.isEmpty()) {
                throw new RuntimeException("로그인 정보가 존재하지 않습니다.");
            }
            // db 에서 가져온 refresh가 cookie 의 refresh 랑 다르면 에러
            if(!dbToken.equals(getToken(request, refreshCookieName))) {
                throw new RuntimeException("로그인 정보가 존재하지 않습니다.");
            }

            return addAuthCookies(response, refreshToken.get());
        }

        Jws<Claims> accessTokenClaims = accessToken.get();

        return Long.parseLong(accessTokenClaims.getBody().getSubject());
    }

    /**
     * access 토큰 만료 시, 인증 관련 쿠키 추가
     */
    private Long addAuthCookies(HttpServletResponse response, Jws<Claims> refreshTokenValue) {

        // refresh 토큰 유효
        Long memberId = Long.parseLong(refreshTokenValue.getBody().getSubject());


        String newAccessToken = tokenProvider.generateToken(memberId);
        Cookie newAccessTokenCookie = new Cookie(accessCookieName, newAccessToken);
        response.addCookie(newAccessTokenCookie);


        String newRefreshToken = tokenProvider.generateRefreshToken(memberId);

        GenerateRefreshTokenParam generateRefreshTokenParam = new GenerateRefreshTokenParam();
        generateRefreshTokenParam.setToken(newRefreshToken);
        generateRefreshTokenParam.setMemberId(memberId);
        authService.generateRefreshToken(generateRefreshTokenParam);

        Cookie newRefreshTokenCookie = new Cookie(refreshCookieName, newRefreshToken);
        response.addCookie(newRefreshTokenCookie);
        return memberId;
    }


    private Optional<Jws<Claims>> getClaimFromToken(HttpServletRequest request, String tokenName) {
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (!tokenName.equals(cookie.getName())) {
                continue;
            }
            return Optional.ofNullable(tokenProvider.getClaims(cookie.getValue()));
        }
        return Optional.empty();
    }

    private String getToken(HttpServletRequest request, String tokenName) {
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (!tokenName.equals(cookie.getName())) {
                continue;
            }
            return cookie.getValue();
        }
        return null;
    }
}
