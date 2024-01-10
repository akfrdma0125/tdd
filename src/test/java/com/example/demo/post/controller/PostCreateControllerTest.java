package com.example.demo.post.controller;


import com.example.demo.mock.TestClockHolder;
import com.example.demo.mock.TestContainer;
import com.example.demo.post.controller.response.PostResponse;
import com.example.demo.post.domain.PostCreate;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

class PostCreateControllerTest {
    @Test
    void 게시물을_생성할_수_있다() throws Exception {
        //given
        TestContainer testContainer = TestContainer.builder()
                .clockHolder(TestClockHolder.builder().millis(1000L).build())
                .build();

        testContainer.userRepository.save(
                User.builder()
                        .id(1L)
                        .email("jeohyoo1229@gmail.com")
                        .nickname("jeohyoo1229")
                        .address("서울시 강남구")
                        .status(UserStatus.ACTIVE)
                        .certificationCode("1234-1234-1234-1234")
                        .build());

        PostCreate postCreateDto = PostCreate.builder()
                .writerId(1L)
                .content("안녕하세요")
                .build();
        //when
        ResponseEntity<PostResponse> result = testContainer.postCreateController
                .create(postCreateDto);
        //then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(201));
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().getId()).isEqualTo(1L);
        assertThat(result.getBody().getCreatedAt()).isEqualTo(1000L);
        assertThat(result.getBody().getContent()).isEqualTo("안녕하세요");
        assertThat(result.getBody().getWriter().getEmail()).isEqualTo("jeohyoo1229@gmail.com");
    }
}
