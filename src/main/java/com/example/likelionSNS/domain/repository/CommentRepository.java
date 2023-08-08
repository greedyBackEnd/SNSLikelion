package com.example.likelionSNS.domain.repository;

import com.example.likelionSNS.domain.entity.comment.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
