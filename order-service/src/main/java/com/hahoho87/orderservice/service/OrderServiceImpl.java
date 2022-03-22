package com.hahoho87.orderservice.service;

import com.hahoho87.orderservice.dto.OrderDto;
import com.hahoho87.orderservice.entity.OrderEntity;
import com.hahoho87.orderservice.repository.OrderRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ModelMapper mapper;

    public OrderServiceImpl(OrderRepository orderRepository, ModelMapper mapper) {
        this.orderRepository = orderRepository;
        this.mapper = mapper;
    }

    @Override
    public OrderDto createOrder(OrderDto orderDto) {
        orderDto.setOrderId(UUID.randomUUID().toString());
        orderDto.setTotalPrice(this.getTotalPrice(orderDto));

        OrderEntity order = mapper.map(orderDto, OrderEntity.class);
        orderRepository.save(order);

        return orderDto;
    }

    private int getTotalPrice(OrderDto dto) {
        return dto.getUnitPrice() * dto.getQyt();
    }

    @Override
    public OrderDto getOrderByOrderId(String orderId) {
        OrderEntity order = orderRepository.findByOrderId(orderId);
        return mapper.map(order, OrderDto.class);
    }

    @Override
    public List<OrderEntity> getOrdersByUserId(String userId) {
        return orderRepository.findByUserId(userId);
    }
}
