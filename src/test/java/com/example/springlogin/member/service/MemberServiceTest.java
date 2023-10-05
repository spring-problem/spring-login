package com.example.springlogin.member.service;

import com.example.springlogin.member.domain.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

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


    //로그인을 시도해서 로그인에 성공하는지 테스트
    @Test
    void 로그인_성공(){
        //given
        String mail = "test1@gmail.com";
        String password = "1234";

        String mail2 = "test1@gmail.com";
        String password2 = "1234";

        JoinParam joinParam = JoinParam.builder()
                .email(mail)
                .password(password)
                .build();

        LoginParam loginParam = LoginParam.builder()
                .email(mail2)
                .password(password2)
                .build();
        //when
        memberService.join(joinParam);
        Optional<Member> member = memberService.login(loginParam);

        //then
        assertTrue(member.isPresent());
        //boolean 값을 return하는 메소드를 테스트하기에 적합함.
        //true를 반환해야하는 test일시 assertTrue를 사용하고 false를 반환해야하는 test일 시 assertFalse를 반환함.
    }

    @Test
    void 로그인_실패(){
        //given
        String mail = "test1@gmail.com";
        String password = "1234";

        String mail2 = "test1@gmail.com";
        String password2 = "12341234";

        JoinParam joinParam = JoinParam.builder()
                .email(mail)
                .password(password)
                .build();

        LoginParam loginParam = LoginParam.builder()
                .email(mail2)
                .password(password2)
                .build();
        //when
        memberService.join(joinParam);
        Optional<Member> member = memberService.login(loginParam);

        //then
        assertFalse(member.isPresent());
    }

}