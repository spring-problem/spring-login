package com.example.springlogin.auth.repository;

import com.example.springlogin.auth.domain.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthRepository extends JpaRepository<RefreshToken, Long> {
}
