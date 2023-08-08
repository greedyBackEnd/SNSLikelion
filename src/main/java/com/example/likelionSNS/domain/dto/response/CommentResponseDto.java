package com.example.likelionSNS.domain.dto.response;

import com.example.likelionSNS.domain.entity.comment.Comment;
import com.example.likelionSNS.domain.entity.feed.Feed;
import com.example.likelionSNS.domain.entity.user.User;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CommentResponseDto {
    private Long id;
    private User user;
    private Feed feed;
    private String content;

    public static CommentResponseDto of(Comment comment) {
        return CommentResponseDto.builder()
                .id(comment.getId())
                .user(comment.getUser())
                .feed(comment.getFeed())
                .content(comment.getContent())
                .build();
    }
}
