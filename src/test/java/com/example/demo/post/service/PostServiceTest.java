package com.example.demo.post.service;

import com.example.demo.mock.FakePostRepository;
import com.example.demo.mock.FakeUserRepository;
import com.example.demo.mock.TestClockHolder;
import com.example.demo.post.domain.PostCreate;
import com.example.demo.post.domain.PostUpdate;
import com.example.demo.user.domain.UserStatus;
import com.example.demo.user.service.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

 class PostServiceTest {

    private PostService postService;

    @BeforeEach
    void init(){
        FakePostRepository fakePostRepository = new FakePostRepository();
        FakeUserRepository fakeUserRepository = new FakeUserRepository();
        postService = PostService.builder()
                .postRepository(fakePostRepository)
                .userRepository(fakeUserRepository)
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
        fakeUserRepository.save(writer);

        fakePostRepository.save(
                Post.builder()
                        .id(1L)
                        .content("hello, riize")
                        .writer(writer)
                        .build()
        );
    }

    @Test
    void getById는_존재하는_게시물을_내려준다() {
        // given
        // when
        Post result = postService.getById(1);

        // then
        assertThat(result.content()).isEqualTo("hello, riize");
        assertThat(result.writer().email()).isEqualTo("jeohyoo1229@gmail.com");
    }

    @Test
    void postCreateDto_를_이용하여_게시물을_생성할_수_있다() {
        // given
        PostCreate postCreate = PostCreate.builder()
                .writerId(1)
                .content("foobar")
                .build();

        // when
        Post result = postService.create(postCreate);

        // then
        assertThat(result.id()).isNotNull();
        assertThat(result.content()).isEqualTo("foobar");
        assertThat(result.createdAt()).isEqualTo(1679530673958L);
    }

    @Test
    void postUpdateDto_를_이용하여_게시물을_수정할_수_있다() {
        // given
        PostUpdate postUpdate = PostUpdate.builder()
                .content("hello world :)")
                .build();

        // when
        postService.update(1, postUpdate);

        // then
        Post post = postService.getById(1);
        assertThat(post.content()).isEqualTo("hello world :)");
        assertThat(post.modifiedAt()).isEqualTo(1679530673958L);
    }


}
