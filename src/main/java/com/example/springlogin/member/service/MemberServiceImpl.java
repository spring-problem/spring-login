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

        //없을 경우 Member에 member 등록
        Member member = new Member(param.getEmail(), param.getPassword());
        memberRepository.save(member);
    }

    public Member login(LoginParam param){
        //이메일과 password로 member찾기
        Optional<Member> member = Optional.ofNullable(memberRepository.findByEmailAndPassword(param.getEmail(), param.getPassword()));
        if(member.isPresent())  return null; //없으면 null
        else return member.get(); //있으면 member 리턴
    }
}
