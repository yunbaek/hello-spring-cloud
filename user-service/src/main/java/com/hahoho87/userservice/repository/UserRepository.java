package com.hahoho87.userservice.repository;

import com.hahoho87.userservice.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
}
