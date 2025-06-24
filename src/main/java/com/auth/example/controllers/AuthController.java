package com.auth.example.controllers;

import com.auth.example.models.AuthRequest;
import com.auth.example.models.AuthResponse;
import com.auth.example.models.MyUserDetails;
import com.auth.example.models.User;
import com.auth.example.services.JwtService;
import com.auth.example.services.MyUserDetailsService;
import com.auth.example.services.UserService;
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


    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest request) {
        Authentication authentication = authManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        UserDetails user = (UserDetails) authentication.getPrincipal();
        String jwt = jwtService.generateToken(user);
        return ResponseEntity.ok(new AuthResponse(jwt));
    }

    @PostMapping("/register")
    public ResponseEntity<User> register(@Valid @RequestBody AuthRequest request) {

        User user = User.builder().password(request.getPassword()).email(request.getEmail()).emailValidated(false).build();
        userService.createUser(user);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/validate/{code}")
    public ResponseEntity<?> validateEmail(@Valid @RequestBody AuthRequest request, @PathVariable String code) {
        if (code.length() != 6) {
            return ResponseEntity.badRequest().build();
        }
        Integer numericCode = Integer.parseInt(code);
        return ResponseEntity.ok().build();
    }
}
