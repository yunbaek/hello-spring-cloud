package com.hahoho87.orderservice.controller;

import com.hahoho87.orderservice.service.OrderService;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order-service/orders")
public class OrderController {

    private final OrderService orderService;
    private final ModelMapper mapper;

    public OrderController(OrderService orderService, ModelMapper mapper) {
        this.orderService = orderService;
        this.mapper = mapper;
    }

    @GetMapping("health-check")
    public String healthCheck() {
        return "It's Working in Order Service";
    }

    // TODO add order apis
}
