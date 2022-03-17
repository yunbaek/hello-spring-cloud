package com.hahoho87.userservice.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "users")
public class UserEntity {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false, length = 50, unique = true)
    private String email;
    @Column(nullable = false, length = 50)
    private String name;
    @Column(nullable = false, unique = true)
    private String userId;
    @Column(nullable = false, unique = true)
    private String encryptedPwd;

}
