package com.example.demo.post.service;

import com.example.demo.common.service.port.ClockHolder;
import com.example.demo.post.domain.PostCreate;
import com.example.demo.post.domain.PostUpdate;
import com.example.demo.user.service.User;
import lombok.Builder;

import java.time.Clock;

@Builder
public record Post(
        Long id,
        String content,
        Long createdAt,
        Long modifiedAt,
        User writer
) {
    public static Post from(PostCreate postCreate, User writer, ClockHolder clockHolder) {
        return Post.builder()
            .content(postCreate.getContent())
            .createdAt(clockHolder.millis())
            .writer(writer)
            .build();
    }

    public Post update(PostUpdate postUpdate, ClockHolder clockHolder) {
        return Post.builder()
            .id(this.id)
            .content(postUpdate.getContent())
            .createdAt(this.createdAt)
            .modifiedAt(clockHolder.millis())
            .writer(this.writer)
            .build();
    }
}
