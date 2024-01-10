package com.example.demo.user.controller.response;

import com.example.demo.user.domain.UserStatus;
import com.example.demo.user.service.User;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserResponse {
    private Long id;
    private String email;
    private String nickname;
    private UserStatus status;
    private Long lastLoginAt;

    public static UserResponse from(User user) {
        return UserResponse.builder()
                .id(user.id())
                .email(user.email())
                .nickname(user.nickname())
                .status(user.status())
                .lastLoginAt(user.lastLoginAt())
                .build();
    }
}
