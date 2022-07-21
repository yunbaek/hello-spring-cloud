package com.hahoho87.orderservice.controller;

import com.hahoho87.orderservice.dto.OrderDto;
import com.hahoho87.orderservice.entity.OrderEntity;
import com.hahoho87.orderservice.service.OrderService;
import com.hahoho87.orderservice.vo.OrderRequest;
import com.hahoho87.orderservice.vo.OrderResponse;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/order-service/orders")
public class OrderController {

    private final OrderService orderService;
    private final ModelMapper mapper;

    public OrderController(OrderService orderService, ModelMapper mapper) {
        this.orderService = orderService;
        this.mapper = mapper;
    }

    @GetMapping("/health-check")
    public String healthCheck() {
        return "It's Working in Order Service";
    }

    @PostMapping("/{userId}")
    public ResponseEntity<OrderResponse> createOrder(@RequestBody OrderRequest order,
                                                     @PathVariable String userId) {
        OrderDto orderDto = getMap(order, OrderDto.class);

        orderDto.setUserId(userId);
        OrderDto createdOrder = orderService.createOrder(orderDto);


        OrderResponse result = getMap(createdOrder, OrderResponse.class);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<OrderResponse>> getOrders(@PathVariable String userId) {
        List<OrderEntity> orders = orderService.getOrdersByUserId(userId);
        List<OrderResponse> result = orders.stream().map(o -> getMap(o, OrderResponse.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }


    public <D> D getMap(Object source, Class<D> destinationType) {
        return mapper.map(source, destinationType);
    }
}
