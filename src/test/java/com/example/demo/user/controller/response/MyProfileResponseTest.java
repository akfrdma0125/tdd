package com.example.demo.user.controller.response;

import com.example.demo.user.domain.UserStatus;
import com.example.demo.user.domain.User;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MyProfileResponseTest {
    @Test
    void User로_MyProfileResponse_응답을_생성할_수_있다() {
        //given
        User user = User.builder()
                .id(1L)
                .email("jeohyoo1229@gmail.com")
                .nickname("jeohyoo1229")
                .address("서울시 강남구")
                .status(UserStatus.ACTIVE)
                .certificationCode("1234-1234-1234-1234")
                .build();

        //when
        MyProfileResponse myProfileResponse = MyProfileResponse.from(user);

        //then
        assertThat(myProfileResponse.getId()).isEqualTo(1L);
        assertThat(myProfileResponse.getEmail()).isEqualTo("jeohyoo1229@gmail.com");
        assertThat(myProfileResponse.getNickname()).isEqualTo("jeohyoo1229");
        assertThat(myProfileResponse.getAddress()).isEqualTo("서울시 강남구");
        assertThat(myProfileResponse.getStatus()).isEqualTo(UserStatus.ACTIVE);
    }
}