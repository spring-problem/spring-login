package com.example.springlogin.global.filter;

import com.example.springlogin.global.util.auth.AuthUtil;
import com.example.springlogin.member.domain.Member;
import com.example.springlogin.member.domain.Role;
import com.example.springlogin.member.service.MemberService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@RequiredArgsConstructor
public class AuthFilter extends OncePerRequestFilter {

    final AuthUtil authUtil;
    final MemberService memberService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        Long memberId = authUtil.getMemberId(request, response);
        Optional<Member> loginUserById = memberService.getLoginUserById(memberId);
        if (loginUserById.isEmpty()) {
            throw new RuntimeException("유저가 존재하지 않습니다.");
        }
        Member findMember = loginUserById.get();

        if (!Role.ADMIN.equals(findMember.getRole())) {
            throw new RuntimeException("접근 권한이 없습니다.");
        }

        filterChain.doFilter(request, response);
    }
}
