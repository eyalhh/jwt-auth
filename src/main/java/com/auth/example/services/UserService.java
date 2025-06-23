package com.auth.example.services;

import com.auth.example.models.User;
import com.auth.example.repos.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User createUser(User user) {
        User populatedUser = userRepository.save(user);
        log.info("created this user: {}", populatedUser);
        return populatedUser;
    }
}
