package com.example.demo.post.controller.response;

import com.example.demo.post.domain.Post;
import com.example.demo.user.controller.response.UserResponse;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PostResponse {
    private Long id;
    private String content;
    private Long createdAt;
    private Long modifiedAt;
    private UserResponse writer;

    public static PostResponse from(Post post) {
        return PostResponse.builder()
            .id(post.id())
            .content(post.content())
            .createdAt(post.createdAt())
            .modifiedAt(post.modifiedAt())
            .writer(UserResponse.from(post.writer()))
            .build();
    }
}
