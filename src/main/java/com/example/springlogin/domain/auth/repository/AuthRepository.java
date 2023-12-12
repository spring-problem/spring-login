package com.example.springlogin.domain.auth.repository;

import com.example.springlogin.domain.auth.domain.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthRepository extends JpaRepository<RefreshToken, Long> {
    @Modifying
    @Query(value = "delete from RefreshToken where member.id =:member")
    public void 아무거나(Long member);
}
