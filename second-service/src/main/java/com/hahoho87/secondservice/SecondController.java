package com.hahoho87.secondservice;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/second-service")
public class SecondController {
    @GetMapping("/welcome")
    public String welcome() {
        return "Welcome to the Second Service.";
    }
}