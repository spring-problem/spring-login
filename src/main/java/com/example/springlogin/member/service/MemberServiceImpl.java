package com.example.springlogin.member.service;

import com.example.springlogin.member.domain.Member;
import com.example.springlogin.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
// Autowired + final
@RequiredArgsConstructor
@Service
public class MemberServiceImpl implements MemberService {
    final MemberRepository memberRepository;

    @Override
    public boolean signUp(signUpParam param) {


        Member tmp = memberRepository.findByEmail(param.getEmail());
        if(tmp == null) {
            throw new RuntimeException("중복되는 아이디가 존재합니다.");
        }
        // 회원가입 성공
        memberRepository.save(tmp);
        return true;

    }

    @Override
    public String login(loginParam param) {

        Member tmp = memberRepository.findByEmailAndPassword(param.getEmail(), param.getPassword());
        if (tmp == null) {
            throw new RuntimeException("아이디와 비밀번호를 확인해 주세요");
        }
        // 로그인 성공
        return tmp.getEmail();
    }
}
