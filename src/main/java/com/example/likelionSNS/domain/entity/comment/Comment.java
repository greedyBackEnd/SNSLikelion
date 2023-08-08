package com.example.likelionSNS.domain.entity.comment;


import com.example.likelionSNS.domain.entity.BaseEntity;
import com.example.likelionSNS.domain.entity.feed.Feed;
import com.example.likelionSNS.domain.entity.user.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder
@Table(name = "comment")
public class Comment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    @ManyToOne
    @JoinColumn(name = "feedId")
    private Feed feed;

    @JoinColumn(nullable = false)
    private String content;

    public void updateComment(Comment commentUpdate) {
        this.content = commentUpdate.getContent();
    }
}
