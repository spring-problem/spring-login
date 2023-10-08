package com.example.springlogin.member.service;

import com.example.springlogin.member.domain.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class MemberServiceTest {

    @Autowired
    MemberService memberService;

    @Test
    void 이메일_중복_예외() {
        //given
        String mail = "test1@gmail.com";
        String password = "1234";

        JoinParam param = JoinParam.builder()
                .email(mail)
                .password(password)
                .build();

        JoinParam param2 = JoinParam.builder()
                .email(mail)
                .password(password)
                .build();

        //when
        memberService.join(param);

        //then
        Assertions.assertThatThrownBy(() -> memberService.join(param2))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    void 로그인_실패() {
        // given
        String email1 = "jun@naver.com";
        String pass1 = "1234";

        JoinParam joinParam = JoinParam.builder()
                .email(email1)
                .password(pass1)
                .build();

        memberService.join(joinParam);

        String email2 = "jun@naver.com";
        String pass2 = "4321";

        LoginParam loginParam = LoginParam.builder()
                .email(email2)
                .password(pass2)
                .build();

        // when
        Optional<Member> loggedInMember = memberService.login(loginParam);

        // then
        assertThat(loggedInMember).isEmpty();

    }
    @Test
    void 로그인_성공() {
        // given
        String email1 = "jun@naver.com";
        String pass1 = "1234";

        JoinParam joinParam = JoinParam.builder()
                .email(email1)
                .password(pass1)
                .build();

        memberService.join(joinParam);

        String email2 = "jun@naver.com";
        String pass2 = "1234";

        LoginParam loginParam = LoginParam.builder()
                .email(email2)
                .password(pass2)
                .build();

        // when
        Optional<Member> loggedInMember = memberService.login(loginParam);

        // then
        assertThat(loggedInMember).isPresent();
    }

    @Test
    void 비밀번호_최소길이() {
        // given
        String email1 = "jun@naver.com";
        String pass1 = "123";

        JoinParam joinParam = JoinParam.builder()
                .email(email1)
                .password(pass1)
                .build();
        // when

        // then
    }
    // 이메일 @ . 형식 맞는지 체크

    // 권한이 있다면 권한에 안맞는 동작을 하는지 체크





}