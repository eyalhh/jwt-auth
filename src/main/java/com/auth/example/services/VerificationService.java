package com.auth.example.services;

import com.auth.example.models.EmailVerificationCode;
import com.auth.example.models.User;
import com.auth.example.repos.VerificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
public class VerificationService {

    @Autowired
    private VerificationRepository verificationRepository;
    @Autowired
    private UserService userService;
    public Integer generateRandomCode() {
        Random random = new Random();
        return 100000 + random.nextInt(900000);
    }

    public Boolean verifyCode(User user, Integer code) {
        List<EmailVerificationCode> codes = verificationRepository.findByCode(code);
        for (int i = 0; i < codes.size(); i++) {
            if (codes.get(i).getUser().getEmail().equals(user.getEmail())) {
                userService.verifyUser(user);
                return true;
            }
        }
        return false;
    }
}
