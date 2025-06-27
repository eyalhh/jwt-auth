package com.auth.example.models;

import jakarta.validation.constraints.NotEmpty;

public record VerifiedResponse (
        @NotEmpty
        Integer code
) { }
