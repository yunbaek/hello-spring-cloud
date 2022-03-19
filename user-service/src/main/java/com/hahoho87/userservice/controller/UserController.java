package com.hahoho87.userservice.controller;

import com.hahoho87.userservice.dto.UserDto;
import com.hahoho87.userservice.dto.UserRequestDto;
import com.hahoho87.userservice.dto.UserResponseDto;
import com.hahoho87.userservice.service.UserService;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user-service/users")
public class UserController {

    private final UserService userService;
    private final ModelMapper mapper;

    public UserController(UserService userService, ModelMapper mapper) {
        this.userService = userService;
        this.mapper = mapper;
    }

    @PostMapping
    public ResponseEntity<UserResponseDto> createUser(@RequestBody UserRequestDto requestDto) {
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        UserDto userDto = mapper.map(requestDto, UserDto.class);

        userService.createUser(userDto);
        UserResponseDto result = mapper.map(userDto, UserResponseDto.class);

        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDto>> findAllUsers() {
        List<UserDto> allUsers = userService.findAllUsers();
        return ResponseEntity.status(HttpStatus.OK)
                .body(allUsers.stream()
                        .map(u -> mapper.map(u, UserResponseDto.class))
                        .collect(Collectors.toList()));
    }

    @GetMapping("/health-check")
    public String status(HttpServletRequest request) {
        return String.format("It's Working In User Service on Port %s", request.getServerPort());
    }
}
