package com.hahoho87.catalogservice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/catalog-service")
public class CatalogController {

    @GetMapping("/health-check")
    public String healthCheck(HttpServletRequest request) {
        return String.format("It's Working In Catalog Service on Port %s", request.getServerPort());
    }
}
