package com.auth.example.controllers;

import com.auth.example.models.*;
import com.auth.example.services.JwtService;
import com.auth.example.services.MyUserDetailsService;
import com.auth.example.services.UserService;
import com.auth.example.services.VerificationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/auth")
@RestController
public class AuthController {

    @Autowired private UserService userService;
    @Autowired private JwtService jwtService;
    @Autowired private AuthenticationManager authManager;
    @Autowired private VerificationService verificationService;


    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody AuthRequest request) {

        // authenticate the user
        Authentication authentication = authManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        MyUserDetails userDetails = (MyUserDetails) authentication.getPrincipal();
        User user = userDetails.getUser();

        if (!user.getEmailValidated()) {
            return ResponseEntity.badRequest().build();
        }
        String jwt = jwtService.generateToken(userDetails);
        return ResponseEntity.ok(new AuthResponse(jwt));
    }

    @PostMapping("/register")
    public ResponseEntity<User> register(@Valid @RequestBody AuthRequest request) {

        User user = User.builder().password(request.getPassword()).email(request.getEmail()).emailValidated(false).build();
        userService.createUser(user);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/validate/{code}")
    public ResponseEntity<?> validateEmail(@Valid @RequestBody VerifiedRequest request, @PathVariable String code) {


        if (userService.getUserByEmail(request.email()) == null|| code.length() != 6) {
            return ResponseEntity.badRequest().build();
        }

        Integer numericCode = Integer.parseInt(code);

        if (verificationService.verifyCode(request.email(), numericCode)) {
            User user = User.builder().email(request.email()).build();
            String jwt = jwtService.generateToken(new MyUserDetails(user));
            return ResponseEntity.ok(new AuthResponse(jwt));
        }
        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/get-verified")
    public VerifiedResponse getVerified(@Valid @RequestBody VerifiedRequest request) {
        return new VerifiedResponse(verificationService.generateRandomCode(request.email()));
    }
}
