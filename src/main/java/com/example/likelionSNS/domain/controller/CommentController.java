package com.example.likelionSNS.domain.controller;

import com.example.likelionSNS.domain.dto.request.CommentRegistrationDto;
import com.example.likelionSNS.domain.dto.request.CommentUpdateRequestDto;
import com.example.likelionSNS.domain.dto.response.CommentResponseDto;
import com.example.likelionSNS.domain.service.CommentService;
import com.example.likelionSNS.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/feeds/{feedId}/comments")
public class CommentController {

    private final CommentService commentService;

    // 댓글 등록
    @PostMapping
    public ResponseEntity<Map<String, String>> registerComment(@PathVariable Long feedId,
                                                               @RequestBody CommentRegistrationDto requestDto) {
        String username = SecurityUtils.getCurrentUsername();
        commentService.registerComment(username, feedId, requestDto);

        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("message", "댓글이 작성되었습니다.");
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

    // 댓글 수정
    @PutMapping("/{commentId}")
    public ResponseEntity<CommentResponseDto> updateComment(@PathVariable Long feedId,
                                                            @PathVariable Long commentId,
                                                            @RequestBody CommentUpdateRequestDto requestDto) {
        String username = SecurityUtils.getCurrentUsername();

        CommentResponseDto responseDto = commentService.updateComment(username, feedId, commentId, requestDto);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    // 댓글 삭제
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Map<String, String>> deleteComment(@PathVariable Long feedId,
                                                             @PathVariable Long commentId) {
        String username = SecurityUtils.getCurrentUsername();

        commentService.deleteComment(username, feedId, commentId);

        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("message", "댓글이 삭제되었습니다.");
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }
}
