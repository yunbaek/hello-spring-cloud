package com.hahoho87.orderservice.controller;

import com.hahoho87.orderservice.dto.OrderDto;
import com.hahoho87.orderservice.entity.OrderEntity;
import com.hahoho87.orderservice.messagequeue.KafkaProducer;
import com.hahoho87.orderservice.messagequeue.OrderProducer;
import com.hahoho87.orderservice.service.OrderService;
import com.hahoho87.orderservice.vo.OrderRequest;
import com.hahoho87.orderservice.vo.OrderResponse;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/order-service/orders")
public class OrderController {

    private final OrderService orderService;
    private final ModelMapper mapper;
    private final KafkaProducer kafkaProducer;
    private final OrderProducer orderProducer;

    public OrderController(OrderService orderService, ModelMapper mapper,
                           KafkaProducer kafkaProducer, OrderProducer orderProducer) {
        this.orderService = orderService;
        this.mapper = mapper;
        this.kafkaProducer = kafkaProducer;
        this.orderProducer = orderProducer;
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

        /* kafka */
        orderDto.setOrderId(UUID.randomUUID().toString());
        orderDto.setTotalPrice(this.getTotalPrice(orderDto));

        /* send order to the kafka */
        kafkaProducer.send("example-catalog-topic", orderDto);
        orderProducer.send("orders", orderDto);

        OrderResponse result = getMap(orderDto, OrderResponse.class);
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

    private int getTotalPrice(OrderDto dto) {
        return dto.getUnitPrice() * dto.getQty();
    }

}
