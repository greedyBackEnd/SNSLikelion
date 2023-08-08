package com.example.likelionSNS.domain.service;

import com.example.likelionSNS.domain.dto.request.CommentRegistrationDto;
import com.example.likelionSNS.domain.dto.request.CommentUpdateRequestDto;
import com.example.likelionSNS.domain.dto.response.CommentResponseDto;
import com.example.likelionSNS.domain.entity.comment.Comment;
import com.example.likelionSNS.domain.entity.feed.Feed;
import com.example.likelionSNS.domain.entity.user.User;
import com.example.likelionSNS.domain.repository.CommentRepository;
import com.example.likelionSNS.domain.repository.FeedRepository;
import com.example.likelionSNS.domain.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final FeedRepository feedRepository;

    // 댓글 등록
    @Transactional
    public CommentResponseDto registerComment(String username, Long feedId, CommentRegistrationDto requestDto) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("해당 유저를 찾을 수 없습니다." + username));

        Feed feed = feedRepository.findById(feedId)
                .orElseThrow(() -> new EntityNotFoundException("해당 피드를 찾을 수 없습니다."));


        Comment comment = Comment.builder()
                .user(user)
                .feed(feed)
                .content(requestDto.getContent())
                .build();

        comment = commentRepository.save(comment);

        return CommentResponseDto.of(comment);
    }

    // 댓글 수정
    @Transactional
    public CommentResponseDto updateComment(String username, Long feedId, Long commentId, CommentUpdateRequestDto requestDto) {
        Feed feed = feedRepository.findById(feedId)
                .orElseThrow(() -> new EntityNotFoundException("해당 피드를 찾을 수 없습니다."));

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("해당 댓글을 찾을 수 없습니다."));

        if (!comment.getUser().getUsername().equals(username)) {
            throw new IllegalArgumentException("댓글을 수정할 권한이 없습니다.");
        }

        comment.updateComment(requestDto.toEntity());
        commentRepository.save(comment);

        return CommentResponseDto.of(comment);
    }

    // 댓글 삭제
    @Transactional
    public void deleteComment(String username, Long feedId, Long commentId) {
        Feed feed = feedRepository.findById(feedId)
                .orElseThrow(() -> new EntityNotFoundException("해당 피드를 찾을 수 없습니다."));

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("해당 댓글을 찾을 수 없습니다."));

        if (!comment.getUser().getUsername().equals(username)) {
            throw new IllegalArgumentException("댓글을 삭제할 권한이 없습니다.");
        }

        commentRepository.delete(comment);
    }
}
