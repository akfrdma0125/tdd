package com.example.demo.post.controller.response;

import com.example.demo.post.service.Post;
import com.example.demo.user.domain.UserStatus;
import com.example.demo.user.service.User;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class PostResponseTest {

    @Test
    void Post로_응답을_생성할_수_있다() {
        //given
        User writer = User.builder()
                .id(1L)
                .email("jeohyoo1229@gmail.com")
                .nickname("jeohyoo1229")
                .address("서울시 강남구")
                .status(UserStatus.ACTIVE)
                .certificationCode("1234-1234-1234-1234")
                .build();
        Post post = Post.builder()
                .id(1L)
                .content("hello, riize")
                .writer(writer)
                .build();
        //when
        PostResponse postResponse = PostResponse.from(post);

        //then
        assertThat(postResponse.getContent()).isEqualTo("hello, riize");
        assertThat(postResponse.getWriter().getEmail()).isEqualTo("jeohyoo1229@gmail.com");
        assertThat(postResponse.getWriter().getStatus()).isEqualTo(UserStatus.ACTIVE);
        assertThat(postResponse.getWriter().getNickname()).isEqualTo("jeohyoo1229");
    }
}