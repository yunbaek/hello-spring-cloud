package com.hahoho87.orderservice.service;

import com.hahoho87.orderservice.dto.OrderDto;
import com.hahoho87.orderservice.entity.OrderEntity;

import java.util.List;

public interface OrderService {
    OrderDto createOrder(OrderDto dto);

    OrderDto getOrderByOrderId(String orderId);
    List<OrderEntity> getOrdersByUserId(String userId);
}
