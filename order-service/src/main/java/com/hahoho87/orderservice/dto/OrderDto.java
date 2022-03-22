package com.hahoho87.orderservice.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class OrderDto implements Serializable {
    private String productId;
    private Integer qyt;
    private Integer unitPrice;
    private Integer totalPrice;

    private String orderId;
    private String userId;
}
