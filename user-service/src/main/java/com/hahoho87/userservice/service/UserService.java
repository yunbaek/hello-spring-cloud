package com.hahoho87.userservice.service;

import com.hahoho87.userservice.dto.UserDto;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {

    UserDto createUser(UserDto userDto);

    List<UserDto> findAllUsers();

    UserDto getUserByUserId(String userId);
}
