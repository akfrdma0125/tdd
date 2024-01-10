package com.example.demo.user.controller.response;

import com.example.demo.user.domain.UserStatus;
import com.example.demo.user.service.User;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MyProfileResponse {
    private Long id;
    private String email;
    private String nickname;
    private String address;
    private UserStatus status;
    private Long lastLoginAt;

    public static MyProfileResponse from(User user) {
        return MyProfileResponse.builder()
                .id(user.id())
                .email(user.email())
                .nickname(user.nickname())
                .address(user.address())
                .status(user.status())
                .lastLoginAt(user.lastLoginAt())
                .build();
    }
}
