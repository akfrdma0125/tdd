package com.example.demo.post.service;

import com.example.demo.post.domain.PostCreate;
import com.example.demo.post.domain.PostUpdate;
import com.example.demo.user.service.User;
import lombok.Builder;

@Builder
public record Post(
        Long id,
        String content,
        Long createdAt,
        Long modifiedAt,
        User writer
) {
    public static Post from(PostCreate postCreate, User writer) {
        return Post.builder()
            .content(postCreate.getContent())
            .createdAt(System.currentTimeMillis())
            .writer(writer)
            .build();
    }

    public Post update(PostUpdate postUpdate) {
        return Post.builder()
            .id(this.id)
            .content(postUpdate.getContent())
            .createdAt(this.createdAt)
            .modifiedAt(System.currentTimeMillis())
            .writer(this.writer)
            .build();
    }
}
