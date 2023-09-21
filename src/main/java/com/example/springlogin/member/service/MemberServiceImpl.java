package com.example.springlogin.member.service;

import com.example.springlogin.member.domain.Member;
import com.example.springlogin.member.param.JoinParam;
import com.example.springlogin.member.repository.MemberRepository;
import com.example.springlogin.member.request.JoinRequest;
import com.example.springlogin.member.request.LoginRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MemberServiceImpl implements MemberService {

    @Autowired
    MemberRepository memberRepository;

    public boolean join(JoinRequest joinRequest){
        JoinParam param = new JoinParam();
        param.setEmail(joinRequest.getEmail());
        param.setPassword(joinRequest.getEmail());
        Member tmpMember = memberRepository.findByEmail(param.getEmail());
        if(tmpMember != null) return false; //같은 이메일로 가입한 사람이 있을 경우 return false;

        //없을 경우 Member에 member 등록
        Member member = new Member(joinRequest.getEmail(), joinRequest.getPassword());
        memberRepository.save(member);
        return true;
    }

    public boolean login(LoginRequest loginRequest){
        //이메일과 password로 member찾기
        Member member = memberRepository.findByEmailAndPassword(loginRequest.getEmail(), loginRequest.getPassword());
        if(member == null)  return false; //없으면 false
        else return true; //있으면 true

    }
}
