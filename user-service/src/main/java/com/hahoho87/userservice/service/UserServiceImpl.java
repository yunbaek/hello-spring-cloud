package com.hahoho87.userservice.service;

import com.hahoho87.userservice.client.OrderServiceClient;
import com.hahoho87.userservice.dto.UserDto;
import com.hahoho87.userservice.entity.UserEntity;
import com.hahoho87.userservice.exception.UserNotFoundException;
import com.hahoho87.userservice.repository.UserRepository;
import com.hahoho87.userservice.vo.OrderResponse;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final ModelMapper mapper;
    private final OrderServiceClient orderServiceClient;
    private final CircuitBreakerFactory circuitBreakerFactory;

    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder,
                           ModelMapper mapper, OrderServiceClient orderServiceClient,
                           CircuitBreakerFactory circuitBreakerFactory) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.mapper = mapper;
        this.orderServiceClient = orderServiceClient;
        this.circuitBreakerFactory = circuitBreakerFactory;
    }

    @Override
    @Transactional
    public UserDto createUser(UserDto userDto) {
        userDto.setUserId(UUID.randomUUID().toString());
        UserEntity userEntity = mapper.map(userDto, UserEntity.class);

        encodePassword(userDto, userEntity);

        userRepository.save(userEntity);
        return null;
    }

    @Override
    public List<UserDto> findAllUsers() {
        List<UserEntity> users = userRepository.findAll();
        return users.stream()
                .map(u -> mapper.map(u, UserDto.class)).collect(Collectors.toList());
    }

    @Override
    public UserDto getUserByUserId(String userId) {
        Optional<UserEntity> byUserId = userRepository.findByUserId(userId);
        UserEntity userEntity = byUserId.orElseThrow(() ->
                new UserNotFoundException("Can not find User by UserId : " + userId));

        UserDto userDto = mapper.map(userEntity, UserDto.class);

        // Using FeignClient to get orders of user
        CircuitBreaker circuitbreaker = circuitBreakerFactory.create("circuitbreaker");
        List<OrderResponse> orderResponses = circuitbreaker.run(() -> orderServiceClient.getOrders(userId),
                throwable -> new ArrayList<>());
        userDto.setOrderResponses(orderResponses);

        return userDto;
    }

    private void encodePassword(UserDto userDto, UserEntity userEntity) {
        String encodedPassword = passwordEncoder.encode(userDto.getPwd());
        userEntity.setEncryptedPwd(encodedPassword);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UserNotFoundException("User " + username + " is not found."));
        return new User(user.getEmail(), user.getEncryptedPwd(),
                true, true, true, true, new ArrayList<>());
    }

    @Override
    public UserDto getUserDetailsByEmail(String email) {
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Can not find User by Email : " + email));
        return mapper.map(user, UserDto.class);
    }
}
