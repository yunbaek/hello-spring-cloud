package com.hahoho87.userservice.service;

import com.hahoho87.userservice.dto.UserDto;

import java.util.List;

public interface UserService {
    UserDto createUser(UserDto userDto);

    List<UserDto> findAllUsers();

    UserDto getUserByUserId(String userId);
}
