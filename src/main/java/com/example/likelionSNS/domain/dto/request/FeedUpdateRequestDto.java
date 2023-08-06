package com.example.likelionSNS.domain.dto.request;

import com.example.likelionSNS.domain.entity.feed.Feed;
import lombok.Data;

@Data
public class FeedUpdateRequestDto {
    private String title;
    private String content;

    public Feed toEntity() {
        return Feed.builder()
                .title(title)
                .content(content)
                .build();
    }
}
