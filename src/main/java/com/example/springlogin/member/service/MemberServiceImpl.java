package com.example.springlogin.member.service;

import com.example.springlogin.member.domain.Member;
import com.example.springlogin.member.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemberServiceImpl implements MemberService {
    final MemberRepository memberRepository;

    public MemberServiceImpl(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public boolean signUp(signUpParam param) {
        Member member = new Member(param.getEmail(), param.getPassword());

        List<Member> list = memberRepository.findAll();
        for (Member tmp : list) {
            if(tmp.equals(member))
                return false;
        }

        memberRepository.save(member);
        return true;
    }

    @Override
    public String login(loginParam param) {
        Member member = new Member(param.getEmail(), param.getPassword());

        List<Member> list = memberRepository.findAll();
        for (Member tmp : list) {
            if(tmp.equals(member))
                return param.getEmail();
        }

        return null;
    }
}
