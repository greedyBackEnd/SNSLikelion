package com.example.likelionSNS.domain.entity.feed;

import com.example.likelionSNS.domain.entity.BaseEntity;
import com.example.likelionSNS.domain.entity.user.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;

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

    @OneToMany(mappedBy = "feed", cascade = CascadeType.ALL)
    private List<FeedImages> feedImages;

}
