package com.example.springlogin.member.service;

import com.example.springlogin.global.exception.EntityAlreadyExistException;
import com.example.springlogin.member.domain.Member;
import com.example.springlogin.member.repository.MemberRepository;
import com.example.springlogin.member.service.param.JoinParam;
import com.example.springlogin.member.service.param.LoginParam;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    public void join(JoinParam param) {

        Optional<Member> tmpMember = memberRepository.findByEmail(param.getEmail());
        if (tmpMember.isPresent()) throw new EntityAlreadyExistException("이미 존재하는 이메일입니다.");

        Member member = Member.createMember(param.getEmail(), param.getPassword());
        memberRepository.save(member);
    }

    public Optional<Member> login(LoginParam param) {
        return memberRepository.findByEmailAndPassword(param.getEmail(), param.getPassword());
    }

    public List<Member> getAllMembers() {
        return memberRepository.findAll();
    }

    @Override
    public Optional<Member> getLoginUserById(Long id) {
        return memberRepository.findById(id);
    }
}
