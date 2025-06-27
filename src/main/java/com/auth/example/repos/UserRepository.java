package com.auth.example.repos;

import com.auth.example.models.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByEmail(@Param("email") String email);

    @Query("SELECT u FROM User u JOIN FETCH u.tasks WHERE u.id = :id")
    Optional<User> findByIdWithTasks(@Param("id") Integer id);

    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.emailValidated = true WHERE u.email = :email")
    int verifyUserByEmail(@Param("email") String email);

}
