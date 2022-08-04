package com.hahoho87.userservice.controller;

import com.hahoho87.userservice.dto.UserDto;
import com.hahoho87.userservice.service.UserService;
import com.hahoho87.userservice.vo.UserRequest;
import com.hahoho87.userservice.vo.UserResponse;
import io.micrometer.core.annotation.Timed;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final ModelMapper mapper;
    private final Environment environment;

    public UserController(UserService userService, ModelMapper mapper, Environment environment) {
        this.userService = userService;
        this.mapper = mapper;
        this.environment = environment;
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
    @Timed(value="users.status", longTask = true)
    public String status() {
        return String.format("It's Working In User Service"
                        + ", Port(local.server.port) : %s "
                        + ", Port(server.port) : %s "
                        + ", with token secret : %s"
                        + ", with token time : %s"
                , environment.getProperty("local.server.port")
                , environment.getProperty("server.port")
                , environment.getProperty("token.secret")
                , environment.getProperty("token.expiration_time"));
    }
}
