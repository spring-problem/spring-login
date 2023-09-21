package com.example.springlogin.member.repository;

import com.example.springlogin.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Member findByEmailAndPassword(String email, String password);

    Member findByEmail(String email);
}
