package com.example.likelionSNS.domain.dto.request;

import com.example.likelionSNS.domain.entity.comment.Comment;
import lombok.Data;

@Data
public class CommentUpdateRequestDto {
    private String content;

    public Comment toEntity() {
        return Comment.builder()
                .content(content)
                .build();
    }
}
