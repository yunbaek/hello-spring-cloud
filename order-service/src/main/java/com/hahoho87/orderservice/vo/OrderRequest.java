package com.hahoho87.orderservice.vo;

import lombok.Data;

@Data
public class OrderRequest {
    private String productId;
    private Integer qty;
    private Integer unitPrice;
}
