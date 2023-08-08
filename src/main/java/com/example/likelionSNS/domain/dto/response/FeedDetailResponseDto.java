package com.example.likelionSNS.domain.dto.response;

import com.example.likelionSNS.domain.entity.feed.Feed;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class FeedDetailResponseDto {
    private Long id;
    private String username;
    private String title;
    private String content;
    private boolean draft;
    private List<String> imageUrls;
    private List<CommentResponseDto> comments;

    public static FeedDetailResponseDto of(Feed feed) {
        return FeedDetailResponseDto.builder()
                .id(feed.getId())
                .username(feed.getUser().getUsername())
                .title(feed.getTitle())
                .content(feed.getContent())
                .draft(feed.isDraft())
                .build();
    }

    public static FeedDetailResponseDto of(Feed feed, List<String> imageUrls, List<CommentResponseDto> comments) {
        return FeedDetailResponseDto.builder()
                .id(feed.getId())
                .username(feed.getUser().getUsername())
                .title(feed.getTitle())
                .content(feed.getContent())
                .draft(feed.isDraft())
                .imageUrls(imageUrls)
                .comments(comments)
                .build();
    }
}
