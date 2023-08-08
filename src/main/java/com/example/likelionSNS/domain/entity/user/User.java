package com.example.likelionSNS.domain.entity.user;

import com.example.likelionSNS.domain.entity.BaseEntity;
import com.example.likelionSNS.domain.entity.comment.Comment;
import com.example.likelionSNS.domain.entity.feed.Feed;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder
@Table(name = "users")
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    private String profileImg;

    private String phone;

    private String email;

    private String address;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Feed> feeds;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Comment> comments;

    @ManyToMany
    @JoinTable(
            name = "like_feed",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "feed_id")
    )
    private Set<Feed> likedFeeds = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "user_follow",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "follow_id")
    )
    private Set<User> followers = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "friends",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "friend_id")
    )
    private Set<User> friends = new HashSet<>();

    @OneToMany(mappedBy = "requester")
    private List<FriendRequest> friendRequests;

    // 친구 추가
    public void addFriend(User friend) {
        friends.add(friend);
        friend.getFriends().add(this);
    }

    // 친구 삭제
    public void removeFriend(User friend) {
        friends.remove(friend);
        friend.getFriends().remove(this);
    }

    // 팔로우
    public void follow(User targetUser) {
        followers.add(targetUser);
    }

    // 언팔로우
    public void unfollow(User targetUser) {
        followers.remove(targetUser);
    }


    public void updateUser(User userUpdate) {
        if (userUpdate.getPassword() != null) {
            this.password = userUpdate.getPassword();
        }
        this.phone = userUpdate.getPhone();
        this.email = userUpdate.getEmail();
        this.address = userUpdate.getAddress();
    }

    // 프로필 이미지 업데이트
    public void updateProfileImg(String imageUrl) {
        this.profileImg = imageUrl;
    }
}