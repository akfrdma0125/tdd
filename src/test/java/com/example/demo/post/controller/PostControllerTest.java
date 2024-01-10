package com.example.demo.post.controller;


import com.example.demo.common.domain.exception.ResourceNotFoundException;
import com.example.demo.mock.TestClockHolder;
import com.example.demo.mock.TestContainer;
import com.example.demo.post.controller.response.PostResponse;
import com.example.demo.post.domain.Post;
import com.example.demo.post.domain.PostUpdate;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

class PostControllerTest {

    @Test
    void 사용자는_게시물을_조회할_수_있다() throws Exception {
        //given
        TestContainer testContainer = TestContainer.builder().build();
        User writer = User.builder()
                .id(1L)
                .email("jeohyoo1229@gmail.com")
                .nickname("jeohyoo1229")
                .address("서울시 강남구")
                .status(UserStatus.ACTIVE)
                .certificationCode("1234-1234-1234-1234")
                .build();
        testContainer.userRepository.save(writer);
        Post post = Post.builder()
                .id(1L)
                .content("hello, riize")
                .writer(writer)
                .build();
        testContainer.postRepository.save(post);

        //when
        ResponseEntity<PostResponse> result = testContainer.postController.getById(1L);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().getId()).isEqualTo(1L);
        assertThat(result.getBody().getContent()).isEqualTo("hello, riize");
        assertThat(result.getBody().getWriter().getEmail()).isEqualTo("jeohyoo1229@gmail.com");
    }

    @Test
    void 사용자는_존재하지_않는_게시물을_조회할_경우_에러가_난다() throws Exception {
        //given
        TestContainer testContainer = TestContainer.builder().build();

        //when
        //then
        assertThatThrownBy(() -> testContainer.postController.getById(1L))
                .isInstanceOf(ResourceNotFoundException.class);

    }

    @Test
    void 사용자는_게시물을_수정할_수_있다() throws Exception {
        //given
        TestContainer testContainer = TestContainer.builder()
                .clockHolder(TestClockHolder.builder().millis(1679530673958L).build())
                .build();
        User writer = User.builder()
                .id(1L)
                .email("jeohyoo1229@gmail.com")
                .nickname("jeohyoo1229")
                .address("서울시 강남구")
                .status(UserStatus.ACTIVE)
                .certificationCode("1234-1234-1234-1234")
                .build();
        testContainer.userRepository.save(writer);
        Post post = Post.builder()
                .id(1L)
                .content("hello, riize")
                .writer(writer)
                .build();
        testContainer.postRepository.save(post);
        PostUpdate postUpdateDto = PostUpdate.builder()
                .content("안녕하세요")
                .build();
        //when
        ResponseEntity<PostResponse> result = testContainer.postController.update(1L, postUpdateDto);

        //then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().getId()).isEqualTo(1L);
        assertThat(result.getBody().getContent()).isEqualTo("안녕하세요");
        assertThat(result.getBody().getModifiedAt()).isEqualTo(1679530673958L);
        assertThat(result.getBody().getWriter().getEmail()).isEqualTo("jeohyoo1229@gmail.com");
    }
}
