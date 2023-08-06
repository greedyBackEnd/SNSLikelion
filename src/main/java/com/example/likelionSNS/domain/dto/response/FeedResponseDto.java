package com.example.likelionSNS.domain.dto.response;

import com.example.likelionSNS.domain.entity.feed.Feed;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FeedResponseDto {
    private Long id;
    private String username;
    private String title;
    private String content;
    private boolean draft;

    public static FeedResponseDto of(Feed feed) {
        return FeedResponseDto.builder()
                .id(feed.getId())
                .username(feed.getUser().getUsername())
                .title(feed.getTitle())
                .content(feed.getContent())
                .draft(feed.isDraft())
                .build();
    }
}
