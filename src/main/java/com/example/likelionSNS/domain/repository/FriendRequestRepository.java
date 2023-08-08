package com.example.likelionSNS.domain.repository;

import com.example.likelionSNS.domain.entity.user.FriendRequest;
import com.example.likelionSNS.domain.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FriendRequestRepository extends JpaRepository<FriendRequest, Long> {
    List<FriendRequest> findByReceiver(User user);
}
