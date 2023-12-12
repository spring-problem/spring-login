package com.example.springlogin.jwt;

import com.example.springlogin.domain.auth.service.AuthService;
import com.example.springlogin.domain.auth.service.param.GenerateRefreshTokenParam;
import com.example.springlogin.domain.member.domain.Member;
import com.example.springlogin.global.exception.JwtParsingFailException;
import com.example.springlogin.global.util.TokenProvider;
import jakarta.servlet.http.Cookie;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@SpringBootTest
@ActiveProfiles("token-provider-test")
@AutoConfigureMockMvc
public class JwtTest {

    private final TokenProvider provider;
    private final AuthService authService;

    @Autowired
    MockMvc mvc;

    @Autowired
    public JwtTest(TokenProvider provider, AuthService authService) {
        this.provider = provider;
        this.authService = authService;
    }

    @Test
    void TokenProvider_사용_토큰_생성() {
        Member member = Member.createMember("test@naver.com", "password");
        ReflectionTestUtils.setField(member, "id", 1L);
        System.out.println(provider.generateToken(member.getId()));
    }

    @Test
    void 로그인_시_refreshtoken_발급_확인(
            @Value("${auth.jwt.access-cookie-key}") String accessCookieName,
            @Value("${auth.jwt.refresh-cookie-key}") String refreshCookieName
    ) throws Exception {
        MvcResult mvcResult = mvc.perform(
                MockMvcRequestBuilders.post("/login")
                        .contentType("application/x-www-form-urlencoded")
                        .content("email=admin%40a.com&password=admin")
        ).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        String accessToken = response.getCookie(accessCookieName).getValue();
        String refreshToken = response.getCookie(refreshCookieName).getValue();
    }

    @Test
    void accessToken_error_발생시_재발급(
            @Value("${auth.jwt.access-cookie-key}") String accessCookieName,
            @Value("${auth.jwt.refresh-cookie-key}") String refreshCookieName
    ) throws Exception {
        // 만료된 accessToken 과 정상인 refreshToken 을 부여했을때 result 에 정상적인 access 토큰이 발급된다.
        String sendAccessToken = provider.generateToken(1L) + "abcde";
        String sendRefreshToken = provider.generateRefreshToken(1L);

        // db 에 refreshToken 저장 해놓기
        GenerateRefreshTokenParam generateRefreshTokenParam = new GenerateRefreshTokenParam();
        generateRefreshTokenParam.setMemberId(1L);
        generateRefreshTokenParam.setToken(sendRefreshToken);
        authService.generateRefreshToken(generateRefreshTokenParam);

        Thread.sleep(1500L);

        Cookie accessToken = new Cookie(accessCookieName, sendAccessToken);
        Cookie refreshToken = new Cookie(refreshCookieName, sendRefreshToken);

        Cookie[] cookies = {accessToken, refreshToken};

        MvcResult mvcResult = mvc.perform(
                MockMvcRequestBuilders.post("/members")
                        .cookie(cookies)
        ).andReturn();
        
        MockHttpServletResponse response = mvcResult.getResponse();
        String receivedAccessToken = response.getCookie(accessCookieName).getValue();

        assertNotEquals(sendAccessToken,receivedAccessToken);
    }

    @Test
    void TokenProvider_validate_메서드_테스트() throws InterruptedException {
        //given

        Member member = Member.createMember("test@naver.com", "password");
        ReflectionTestUtils.setField(member, "id", 1L);

        //when

        String token = provider.generateToken(member.getId());

        //then

        //signature 변경
        Assertions.assertThatThrownBy(() -> provider.getClaims(token + "123"))
                .isInstanceOf(JwtParsingFailException.class);

        //토큰 만료
        Thread.sleep(1500L);
        Assertions.assertThatThrownBy(() -> provider.getClaims(token))
                .isInstanceOf(JwtParsingFailException.class);
    }

}
