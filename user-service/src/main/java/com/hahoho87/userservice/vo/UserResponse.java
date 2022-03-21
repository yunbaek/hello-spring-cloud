package com.hahoho87.userservice.vo;

import lombok.Data;

import java.util.List;

@Data
public class UserResponse {
    private String email;
    private String name;
    private String userId;

    private List<OrderResponse> orderResponses;
}
