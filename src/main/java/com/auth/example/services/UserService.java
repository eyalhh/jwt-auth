package com.auth.example.services;

import com.auth.example.models.Task;
import com.auth.example.models.User;
import com.auth.example.repos.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@Slf4j
public class UserService {
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;

    public User createUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User populatedUser = userRepository.save(user);
        log.info("created this user: {}", populatedUser);
        return populatedUser;
    }
    public List<Task> getUserTasks(User user) {
        User populatedUser = userRepository.findByIdWithTasks(user.getId()).orElseThrow();
        return populatedUser.getTasks();
    }
    public void verifyUserByEmail(String email) {
        userRepository.verifyUserByEmail(email);
    }
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }
}
