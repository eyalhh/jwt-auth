package com.auth.example.services;

import com.auth.example.models.EmailVerificationCode;
import com.auth.example.models.User;
import com.auth.example.repos.VerificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.NoSuchElementException;
import java.util.Random;

@Service
public class VerificationService {
    private static Integer EXPIRED_IN = 5;

    @Autowired
    private VerificationRepository verificationRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    public void generateRandomCode(String email) {

        Random random = new Random();
        User user = userService.getUserByEmail(email);
        if (user == null || user.getEmailValidated()) throw new NoSuchElementException();
        Integer actualCode = 100000 + random.nextInt(900000);
        EmailVerificationCode code = EmailVerificationCode.builder()
                .code(actualCode)
                .used(false)
                .expires_at(OffsetDateTime.now(ZoneOffset.UTC)
                        .plusMinutes(1)
                        .withNano(0))
                .user(user)
                .build();
        verificationRepository.save(code);
        emailService.sendSimpleEmail(email, "##IMPORTANT##: Verification code for simpleAuth", "Hi, your verification code is : " + actualCode.toString());
        return;

    }

    public Boolean verifyCode(String email, Integer code) {

        User user = userService.getUserByEmail(email);
        if (user == null || user.getEmailValidated()) return false;

        EmailVerificationCode token = verificationRepository.findByEmailAndCode(email, code).orElse(null);
        if (token == null) return false;
        if (token.getExpires_at().isBefore(OffsetDateTime.now(ZoneOffset.UTC))) {
            return false;
        }
        verificationRepository.delete(token);
        userService.verifyUserByEmail(email);
        return true;

    }
    public EmailVerificationCode findByEmailAndCode(String email, Integer code) {
        return verificationRepository.findByEmailAndCode(email, code).orElse(null);
    }
}
