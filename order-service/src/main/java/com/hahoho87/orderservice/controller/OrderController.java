package com.hahoho87.orderservice.controller;

import com.hahoho87.orderservice.dto.OrderDto;
import com.hahoho87.orderservice.entity.OrderEntity;
import com.hahoho87.orderservice.messagequeue.KafkaProducer;
import com.hahoho87.orderservice.messagequeue.OrderProducer;
import com.hahoho87.orderservice.service.OrderService;
import com.hahoho87.orderservice.vo.OrderRequest;
import com.hahoho87.orderservice.vo.OrderResponse;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@Slf4j
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
        log.info("Before add orders data");
        OrderDto orderDto = getMap(order, OrderDto.class);
        orderDto.setUserId(userId);

        /* jpa */
        OrderDto createOrder = orderService.createOrder(orderDto);
        OrderResponse orderResponse = mapper.map(createOrder, OrderResponse.class);

        /* kafka */
//        orderDto.setOrderId(UUID.randomUUID().toString());
//        orderDto.setTotalPrice(this.getTotalPrice(orderDto));

        /* send order to the kafka */
//        kafkaProducer.send("example-catalog-topic", orderDto);
//        orderProducer.send("orders", orderDto);
//        OrderResponse orderResponse = getMap(orderDto, OrderResponse.class);

        log.info("After add orders data");
        return ResponseEntity.status(HttpStatus.CREATED).body(orderResponse);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<OrderResponse>> getOrders(@PathVariable String userId) {
        log.info("Before retrieve orders data");
        List<OrderEntity> orders = orderService.getOrdersByUserId(userId);
        List<OrderResponse> result = orders.stream().map(o -> getMap(o, OrderResponse.class))
                .collect(Collectors.toList());

        orders.stream().map(o -> getMap(o, OrderResponse.class))
                .collect(Collectors.toList());

        try {
            Thread.sleep(1000);
            throw new RuntimeException("Error");
        } catch (InterruptedException e) {
            log.warn(e.getMessage());
        }

        log.info("After retrieve orders data");
        return ResponseEntity.ok(result);
    }


    public <D> D getMap(Object source, Class<D> destinationType) {
        return mapper.map(source, destinationType);
    }

    private int getTotalPrice(OrderDto dto) {
        return dto.getUnitPrice() * dto.getQty();
    }

}
