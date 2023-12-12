package com.example.springlogin.domain.auth.repository;

import com.example.springlogin.domain.auth.domain.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthRepository extends JpaRepository<RefreshToken, Long> {
    @Modifying
    @Query(value = "delete from RefreshToken where member.id =:member")
    void deleteMemberToken(@Param("member") Long member);

    @Modifying
    @Query(value = "select token from RefreshToken where member.id =:member")
    String getMemberToken(@Param("member") Long member);
}
