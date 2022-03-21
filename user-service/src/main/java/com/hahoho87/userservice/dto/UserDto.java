package com.hahoho87.userservice.dto;

import com.hahoho87.userservice.vo.OrderResponse;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class UserDto {
    private String email;
    private String pwd;
    private String name;
    private String userId;
    private LocalDateTime createdAt;

    private String encryptedPwd;
    private List<OrderResponse> orderResponses;
}
