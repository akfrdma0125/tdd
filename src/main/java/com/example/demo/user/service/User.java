package com.example.demo.user.service;

import com.example.demo.common.domain.exception.CertificationCodeNotMatchedException;
import com.example.demo.user.domain.UserCreate;
import com.example.demo.user.domain.UserStatus;
import com.example.demo.user.domain.UserUpdate;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@Builder
@Slf4j
public record User(Long id,
                   String email,
                   String nickname,
                   String address,
                   String certificationCode,
                   UserStatus status,
                   Long lastLoginAt) {
    public static User from(UserCreate userCreate) {
        return User.builder()
                .email(userCreate.getEmail())
                .nickname(userCreate.getNickname())
                .certificationCode(UUID.randomUUID().toString())
                .address(userCreate.getAddress())
                .status(UserStatus.PENDING)
                .build();
    }

    public User update(UserUpdate userUpdate) {
        return User.builder()
                .id(this.id)
                .email(this.email)
                .nickname(userUpdate.getNickname())
                .address(userUpdate.getAddress())
                .certificationCode(this.certificationCode)
                .status(this.status)
                .lastLoginAt(this.lastLoginAt)
                .build();
    }

    public User login(){
        return User.builder()
                .id(this.id)
                .email(this.email)
                .nickname(this.nickname)
                .address(this.address)
                .certificationCode(this.certificationCode)
                .status(this.status)
                .lastLoginAt(System.currentTimeMillis())
                .build();
    }

    public User certificate(String code){
        if (!code.equals(this.certificationCode)) {
            throw new CertificationCodeNotMatchedException();
        }

        return User.builder()
                .id(this.id)
                .email(this.email)
                .nickname(this.nickname)
                .address(this.address)
                .certificationCode(this.certificationCode)
                .status(UserStatus.ACTIVE)
                .lastLoginAt(System.currentTimeMillis())
                .build();
    }
}
