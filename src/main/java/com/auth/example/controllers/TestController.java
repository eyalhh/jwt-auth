package com.auth.example.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/secured-resource")
    public String securedResource() {
        return "secured";
    }
}
