package com.example.springlogin.member.service;

import com.example.springlogin.member.domain.Member;
import com.example.springlogin.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    public void join(JoinParam param){

        Member tmpMember = memberRepository.findByEmail(param.getEmail());
        if(tmpMember != null) throw new RuntimeException("이미 존재하는 이메일입니다.");

        Member member = new Member(param.getEmail(), param.getPassword());
        memberRepository.save(member);
    }

    public Optional<Member> login(LoginParam param){
        Optional<Member> member = Optional.ofNullable(memberRepository.findByEmailAndPassword(param.getEmail(), param.getPassword()));
        if(member.isPresent())  return null;
        else return Optional.ofNullable(member.get());
    }
}
