package com.example.likelionSNS.domain.dto.response;

import com.example.likelionSNS.domain.entity.feed.Feed;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FeedListResponseDto {
    private Long id;
    private String username;
    private String title;
    private String imageUrl;

    public static FeedListResponseDto of(Feed feed, String imageUrl) {
        return FeedListResponseDto.builder()
                .id(feed.getId())
                .username(feed.getUser().getUsername())
                .title(feed.getTitle())
                .imageUrl(imageUrl)
                .build();
    }
}
