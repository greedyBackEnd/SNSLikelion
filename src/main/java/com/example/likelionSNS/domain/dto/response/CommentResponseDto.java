package com.example.likelionSNS.domain.dto.response;

import com.example.likelionSNS.domain.entity.comment.Comment;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CommentResponseDto {
    private Long id;
    private String username;
    private String content;

    public static CommentResponseDto of(Comment comment) {
        return CommentResponseDto.builder()
                .id(comment.getId())
                .username(comment.getUser().getUsername())
                .content(comment.getContent())
                .build();
    }
}
