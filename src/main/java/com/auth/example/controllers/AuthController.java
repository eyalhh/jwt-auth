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
import org.springframework.security.oauth2.core.user.OAuth2User;
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
            return ResponseEntity.status(403).build();
        }
        String jwt = jwtService.generateToken(user.getEmail());
        return ResponseEntity.ok(new AuthResponse(jwt));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody AuthRequest request) {
        User existingUser = userService.getUserByEmail(request.getEmail());
        if (existingUser != null) return ResponseEntity.status(422).build();
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
            String jwt = jwtService.generateToken(request.email());
            return ResponseEntity.ok(new AuthResponse(jwt));
        }
        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/validate")
    public ResponseEntity<?> getVerified(@Valid @RequestBody VerifiedRequest request) {
        verificationService.generateRandomCode(request.email());
        return ResponseEntity.ok().build();
    }

    @RequestMapping("/oauth-success")
    public ResponseEntity<?> oauthSuccess(Authentication authentication) {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        if (oAuth2User == null) return ResponseEntity.badRequest().build();
        String email = oAuth2User.getAttribute("email");

        String jwt = jwtService.generateToken(email);

        return ResponseEntity.ok(new AuthResponse(jwt));

    }

}
