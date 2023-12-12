package com.example.springlogin.jwt;

import com.example.springlogin.domain.member.domain.Member;
import com.example.springlogin.global.exception.JwtParsingFailException;
import com.example.springlogin.global.util.TokenProvider;
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

@SpringBootTest
@ActiveProfiles("token-provider-test")
@AutoConfigureMockMvc
public class JwtTest {

    private final TokenProvider provider;

    @Autowired
    MockMvc mvc;

    @Autowired
    public JwtTest(TokenProvider provider) {
        this.provider = provider;
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
