package com.example.demo.user.domain;

import com.example.demo.common.domain.exception.CertificationCodeNotMatchedException;
import com.example.demo.common.service.port.ClockHolder;
import com.example.demo.common.service.port.UuidHolder;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

@Builder
@Slf4j
public record User(Long id,
                   String email,
                   String nickname,
                   String address,
                   String certificationCode,
                   UserStatus status,
                   Long lastLoginAt) {
    public static User from(UserCreate userCreate, UuidHolder uuidHolder) {
        return User.builder()
                .email(userCreate.getEmail())
                .nickname(userCreate.getNickname())
                .certificationCode(uuidHolder.random())
                .address(userCreate.getAddress())
                .status(UserStatus.PENDING)
                .build();
    }

    public User update(UserUpdate userUpdate, ClockHolder clockHolder) {
        return User.builder()
                .id(this.id)
                .email(this.email)
                .nickname(userUpdate.getNickname())
                .address(userUpdate.getAddress())
                .certificationCode(this.certificationCode)
                .status(this.status)
                .lastLoginAt(clockHolder.millis())
                .build();
    }

    public User login(ClockHolder clockHolder) {
        return User.builder()
                .id(this.id)
                .email(this.email)
                .nickname(this.nickname)
                .address(this.address)
                .certificationCode(this.certificationCode)
                .status(this.status)
                .lastLoginAt(clockHolder.millis())
                .build();
    }

    public User certificate(String code, ClockHolder clockHolder){
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
                .lastLoginAt(clockHolder.millis())
                .build();
    }
}
