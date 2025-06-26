package com.auth.example.repos;

import com.auth.example.models.EmailVerificationCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface VerificationRepository extends JpaRepository<EmailVerificationCode, Integer> {

    @Query("SELECT v FROM EmailVerificationCode v WHERE v.user.email = :email AND v.code = :code")
    Optional<EmailVerificationCode> findByEmailAndCode(@Param("email") String email, @Param("code") Integer code);


}
