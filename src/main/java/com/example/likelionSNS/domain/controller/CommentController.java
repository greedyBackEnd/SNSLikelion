package com.example.likelionSNS.domain.controller;

import com.example.likelionSNS.domain.dto.request.CommentRegistrationDto;
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
}
