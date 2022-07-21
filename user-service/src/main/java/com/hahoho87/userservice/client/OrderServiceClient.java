package com.hahoho87.userservice.client;

import com.hahoho87.userservice.vo.OrderResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "order-service")
public interface OrderServiceClient {

    @GetMapping("/order-service/orders/{userId}")
    List<OrderResponse> getOrders(@PathVariable String userId);
}
