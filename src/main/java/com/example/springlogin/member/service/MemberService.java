package com.example.springlogin.member.service;

import com.example.springlogin.member.domain.Member;

public interface MemberService {
    // 1. 회원가입 -> ture or false 가 넘어가야 한다 ( controller로 )
    boolean signUp(signUpParam param);
    // 2. 로그인 기능 -> email이 넘어가야 한다 ( controller로 )
    String login(loginParam param);
    // 3. 로그아웃 기능 (세션 쿠키를 만료시키는데 -> 로그인을 할때 DB에 접근을 하고 돌아오는 일종의 객체로 로그인을 해놓잖아 그 객체는 이미 브라우저에 있기 때문에 그 해당 객체를 없애주면 된다 ?)

    // 4. 회원 선별 관리자 or 사용자 (이건 회원가입때 일종의 체크박스 같은거를 이용해서 회원 가입에 구현 될거같아 -> DB에 boolean 걸로 관리자 사용자 구분)

}
