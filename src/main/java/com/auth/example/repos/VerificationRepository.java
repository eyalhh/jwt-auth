package com.auth.example.repos;

import com.auth.example.models.EmailVerificationCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VerificationRepository extends JpaRepository<EmailVerificationCode, Integer> {
    public List<EmailVerificationCode> findByCode(Integer Code);
}
