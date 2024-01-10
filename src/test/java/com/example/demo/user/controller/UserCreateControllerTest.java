package com.example.demo.user.controller;


import com.example.demo.mock.TestClockHolder;
import com.example.demo.mock.TestContainer;
import com.example.demo.mock.TestUuidHolder;
import com.example.demo.user.controller.response.UserResponse;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserCreate;
import com.example.demo.user.domain.UserStatus;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

class UserCreateControllerTest {


    @Test
    void 사용자는_회원가입할_수_있고_상태는_PENDING_상태이다() throws Exception {
        //given
        TestContainer testContainer = TestContainer.builder()
                .clockHolder(TestClockHolder.builder().millis(1000L).build())
                .uuidHolder(new TestUuidHolder("test-uuid"))
                .build();
        testContainer.userRepository.save(
                User.builder()
                        .id(1L)
                        .email("jeohyoo1229@gmail.com")
                        .nickname("jeohyoo1229")
                        .address("서울시 강남구")
                        .status(UserStatus.ACTIVE)
                        .certificationCode("1234-1234-1234-1234")
                        .build()
        );
        UserCreate userCreateDto = UserCreate.builder()
                        .email("jeohyoo1229@gmail.com")
                        .nickname("jeohyoo1229")
                        .address("서울시 서대문구")
                        .build();

        //when
        ResponseEntity<UserResponse> result = testContainer.userCreateController
                .create(userCreateDto);

        //then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(201));
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().getId()).isEqualTo(1L);
        assertThat(result.getBody().getNickname()).isEqualTo("jeohyoo1229");
        assertThat(result.getBody().getEmail()).isEqualTo("jeohyoo1229@gmail.com");

    }
}
