package com.example.likelionSNS.domain.repository;

import com.example.likelionSNS.domain.entity.feed.Feed;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedRepository extends JpaRepository<Feed, Long> {
}
