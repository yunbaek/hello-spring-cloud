package com.hahoho87.userservice.service;

import com.hahoho87.userservice.dto.UserDto;
import com.hahoho87.userservice.entity.UserEntity;
import com.hahoho87.userservice.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public UserDto createUser(UserDto userDto) {
        userDto.setUserId(UUID.randomUUID().toString());

        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        UserEntity userEntity = mapper.map(userDto, UserEntity.class);

        encodePassword(userDto, userEntity);

        userRepository.save(userEntity);
        return null;
    }

    private void encodePassword(UserDto userDto, UserEntity userEntity) {
        String encodedPassword = passwordEncoder.encode(userDto.getPwd());
        userEntity.setEncryptedPwd(encodedPassword);
    }
}
