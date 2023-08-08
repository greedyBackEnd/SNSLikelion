package com.example.likelionSNS.domain.repository;

import com.example.likelionSNS.domain.entity.user.FriendRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FriendRequestRepository extends JpaRepository<FriendRequest, Long> {
}
