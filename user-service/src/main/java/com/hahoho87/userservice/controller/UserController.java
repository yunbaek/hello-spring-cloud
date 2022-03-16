package com.hahoho87.userservice.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class UserController {

    @Value("${greeting.message}")
    private String welcomeMessage;

    @GetMapping("/health_check")
    public String healthCheck() {
        return "It's Working in User Service";
    }

    @GetMapping("/welcome")
    public String welcome() {
        return welcomeMessage;
    }
}
