package com.example.springlogin.member.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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

}