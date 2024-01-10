package com.example.demo.post.service;

import com.example.demo.mock.TestClockHolder;
import com.example.demo.post.domain.Post;
import com.example.demo.post.domain.PostCreate;
import com.example.demo.post.domain.PostUpdate;
import com.example.demo.user.domain.UserStatus;
import com.example.demo.user.domain.User;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PostTest {


    @Test
    void PostCreate로_포스트_정보를_생성할_수_있다() {
        //given
        PostCreate postCreate = PostCreate.builder()
            .content("hello, riize")
            .writerId(1L)
            .build();
        User writer = User.builder()
            .id(1L)
            .email("jeohyoo1229@gmail.com")
            .nickname("jeohyoo1229")
            .address("서울시 강남구")
                .status(UserStatus.ACTIVE)
                .certificationCode("1234-1234-1234-1234")
                .build();
        //when
        Post post = Post.from(postCreate, writer, TestClockHolder.builder().millis(1000L).build());
        //then
        assertThat(post.content()).isEqualTo("hello, riize");
        assertThat(post.writer().id()).isEqualTo(1L);
        assertThat(post.writer().email()).isEqualTo("jeohyoo1229@gmail.com");
        assertThat(post.writer().status()).isEqualTo(UserStatus.ACTIVE);
        assertThat(post.writer().certificationCode()).isEqualTo("1234-1234-1234-1234");

    }

    @Test
    void PostUpdate로_포스트_정보를_수정할_수_있다() {
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

        PostUpdate postUpdate = PostUpdate.builder()
                .content("bye, riize")
                .build();

        //when
        post = post.update(postUpdate, TestClockHolder.builder().millis(1000L).build());
        //then
        assertThat(post.content()).isEqualTo("bye, riize");
        assertThat(post.writer().id()).isEqualTo(1L);
        assertThat(post.writer().email()).isEqualTo("jeohyoo1229@gmail.com");
        assertThat(post.writer().status()).isEqualTo(UserStatus.ACTIVE);
        assertThat(post.writer().certificationCode()).isEqualTo("1234-1234-1234-1234");
        assertThat(post.modifiedAt()).isEqualTo(1000L);

    }
}
