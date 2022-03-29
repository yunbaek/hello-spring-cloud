package com.hahoho87.userservice.controller;

import com.hahoho87.userservice.dto.UserDto;
import com.hahoho87.userservice.service.UserService;
import com.hahoho87.userservice.vo.UserRequest;
import com.hahoho87.userservice.vo.UserResponse;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final ModelMapper mapper;

    public UserController(UserService userService, ModelMapper mapper) {
        this.userService = userService;
        this.mapper = mapper;
    }

    @PostMapping
    public ResponseEntity<UserResponse> createUser(@RequestBody UserRequest requestDto) {
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        UserDto userDto = mapper.map(requestDto, UserDto.class);

        userService.createUser(userDto);
        UserResponse result = mapper.map(userDto, UserResponse.class);

        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> findAllUsers() {
        List<UserDto> allUsers = userService.findAllUsers();
        return ResponseEntity.status(HttpStatus.OK)
                .body(allUsers.stream()
                        .map(u -> mapper.map(u, UserResponse.class))
                        .collect(Collectors.toList()));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse> findUserByUserId(@PathVariable String userId) {
        UserDto userDto = userService.getUserByUserId(userId);
        UserResponse result = mapper.map(userDto, UserResponse.class);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @GetMapping("/health-check")
    public String status(HttpServletRequest request) {
        return String.format("It's Working In User Service on Port %s", request.getServerPort());
    }
}
