package com.auth.example.controllers;

import com.auth.example.models.AuthRequest;
import com.auth.example.models.AuthResponse;
import com.auth.example.models.MyUserDetails;
import com.auth.example.models.User;
import com.auth.example.services.JwtService;
import com.auth.example.services.MyUserDetailsService;
import com.auth.example.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/auth")
@RestController
public class AuthController {

    @Autowired private UserService userService;
    @Autowired private MyUserDetailsService userDetailsService;
    @Autowired private JwtService jwtService;
    @Autowired private AuthenticationManager authManager;


    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        Authentication authentication = authManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        UserDetails user = (UserDetails) authentication.getPrincipal();
        String jwt = jwtService.generateToken(user);
        return ResponseEntity.ok(new AuthResponse(jwt));
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody AuthRequest request) {

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        userService.createUser(user);
        UserDetails userDetails = new MyUserDetails(user);
        String jwt = jwtService.generateToken(userDetails);
        return ResponseEntity.ok(new AuthResponse(jwt));
    }
}
