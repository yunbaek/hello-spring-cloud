package com.hahoho87.userservice.controller;

import com.hahoho87.userservice.dto.UserDto;
import com.hahoho87.userservice.dto.UserRequestDto;
import com.hahoho87.userservice.dto.UserResponseDto;
import com.hahoho87.userservice.service.UserService;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<UserResponseDto> createUser(@RequestBody UserRequestDto requestDto) {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        UserDto userDto = mapper.map(requestDto, UserDto.class);

        userService.createUser(userDto);
        UserResponseDto result = mapper.map(userDto, UserResponseDto.class);

        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }
}
