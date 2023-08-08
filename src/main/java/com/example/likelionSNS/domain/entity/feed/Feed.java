package com.example.likelionSNS.domain.entity.feed;

import com.example.likelionSNS.domain.entity.BaseEntity;
import com.example.likelionSNS.domain.entity.comment.Comment;
import com.example.likelionSNS.domain.entity.user.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder
@Table(name = "feed")
public class Feed extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    private boolean draft;

    private LocalDateTime deletedAt;

    @OneToMany(mappedBy = "feed", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<FeedImages> feedImages;

    @OneToMany(mappedBy = "feed", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Comment> comments = new ArrayList<>();

    @ManyToMany(mappedBy = "likedFeeds")
    private Set<User> likers = new HashSet<>();

    public void update(Feed feedUpdate) {
        this.title = feedUpdate.title;
        this.content = feedUpdate.content;
        this.draft = feedUpdate.draft;
    }

    // 임시 저장 피드 배포
    public void publish() {
        this.draft = false;
    }

    // 피드 삭제
    public void delete() {
        this.deletedAt = LocalDateTime.now();
    }

    // 피드 삭제 확인
    public boolean isDeleted() {
        return this.deletedAt != null;
    }
}
