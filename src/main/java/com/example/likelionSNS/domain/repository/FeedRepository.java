package com.example.likelionSNS.domain.repository;

import com.example.likelionSNS.domain.entity.feed.Feed;
import com.example.likelionSNS.domain.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FeedRepository extends JpaRepository<Feed, Long> {
    List<Feed> findByUser(User user);
}
