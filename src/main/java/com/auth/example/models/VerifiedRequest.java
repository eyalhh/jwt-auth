package com.auth.example.models;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

public record VerifiedRequest (
        @Email
        @NotEmpty
        String email
){ }
