package com.hahoho87.orderservice.repository;

import com.hahoho87.orderservice.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
    OrderEntity findByOrderId(String productId);
    List<OrderEntity> findByUserId(String userId);
}
