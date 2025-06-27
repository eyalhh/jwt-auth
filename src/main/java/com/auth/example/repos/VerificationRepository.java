package com.auth.example.repos;

import com.auth.example.models.EmailVerificationCode;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.OffsetDateTime;
import java.util.Optional;

public interface VerificationRepository extends JpaRepository<EmailVerificationCode, Integer> {

    @Query("SELECT v FROM EmailVerificationCode v WHERE v.user.email = :email AND v.code = :code")
    Optional<EmailVerificationCode> findByEmailAndCode(@Param("email") String email, @Param("code") Integer code);

    @Modifying
    @Transactional
    @Query("""
    DELETE FROM EmailVerificationCode e
    WHERE e.expires_at < :now
      AND e.user.email = :email
""")
    void deleteExpiredTokensByEmail(String email, OffsetDateTime now);

    @Query("""
    SELECT COUNT(e) > 0
    FROM EmailVerificationCode e
    WHERE e.user.email = :email
      AND e.expires_at > :now
""")
    Boolean existsUnexpiredTokenByEmail(String email, OffsetDateTime now);



}
