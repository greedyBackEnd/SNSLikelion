package com.example.likelionSNS.domain.entity.user;

import com.example.likelionSNS.domain.entity.BaseEntity;
import com.example.likelionSNS.domain.entity.comment.Comment;
import com.example.likelionSNS.domain.entity.feed.Feed;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

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

    public void updateUser(User userUpdate) {
        if (userUpdate.getPassword() != null) {
            this.password = userUpdate.getPassword();
        }
        this.phone = userUpdate.getPhone();
        this.email = userUpdate.getEmail();
        this.address = userUpdate.getAddress();
    }

    public void updateProfileImg(String imageUrl) {
        this.profileImg = imageUrl;
    }
}