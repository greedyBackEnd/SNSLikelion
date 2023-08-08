package com.example.likelionSNS.domain.controller;


import com.example.likelionSNS.domain.dto.request.FeedRegistrationDto;
import com.example.likelionSNS.domain.dto.request.FeedUpdateRequestDto;
import com.example.likelionSNS.domain.dto.response.FeedDetailResponseDto;
import com.example.likelionSNS.domain.dto.response.FeedListResponseDto;
import com.example.likelionSNS.domain.service.FeedService;
import com.example.likelionSNS.utils.SecurityUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/feeds")
public class FeedController {

    private final FeedService feedService;

    // 피드 임시 저장
    @PostMapping("/draft")
    public ResponseEntity<Map<String, String>> draftFeed(@RequestParam("feed") String feedStr,
                                                         @RequestParam(value = "files", required = false) List<MultipartFile> imageFiles) {
        FeedRegistrationDto requestDto = null;
        try {
            requestDto = new ObjectMapper().readValue(feedStr, FeedRegistrationDto.class);
        } catch (JsonProcessingException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "잘못된 형식의 입력입니다.", e);
        }
        String username = SecurityUtils.getCurrentUsername();

        feedService.registerFeed(username, requestDto, imageFiles == null ? Collections.emptyList() : imageFiles, true);

        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("message", "피드가 임시저장되었습니다.");
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

    // 피드 등록
    @PostMapping
    public ResponseEntity<Map<String, String>> registerFeed(@RequestParam("feed") String feedStr,
                                                            @RequestParam(value = "files", required = false) List<MultipartFile> imageFiles) {
        FeedRegistrationDto requestDto = null;
        try {
            requestDto = new ObjectMapper().readValue(feedStr, FeedRegistrationDto.class);
        } catch (JsonProcessingException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "잘못된 형식의 입력입니다.", e);
        }
        String username = SecurityUtils.getCurrentUsername();

        feedService.registerFeed(username, requestDto, imageFiles == null ? Collections.emptyList() : imageFiles, false);

        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("message", "피드가 등록되었습니다.");
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

    // 피드 단일 조회
    @GetMapping("/{id}")
    public ResponseEntity<FeedDetailResponseDto> getFeed(@PathVariable Long id) {
        return new ResponseEntity<>(feedService.getFeed(id), HttpStatus.OK);
    }

    // 특정 유저 피드 조회
    @GetMapping("/user/{username}")
    public ResponseEntity<List<FeedListResponseDto>> getUserFeeds(@PathVariable String username) {
        return new ResponseEntity<>(feedService.getUserFeeds(username), HttpStatus.OK);
    }

    // 임시 저장된 피드 전체 조회
    @GetMapping("/draft")
    public ResponseEntity<List<FeedListResponseDto>> getDraftFeeds() {
        String username = SecurityUtils.getCurrentUsername();
        return new ResponseEntity<>(feedService.getUserDraftFeeds(username), HttpStatus.OK);
    }

    // 피드 수정
    @PutMapping("/{id}")
    public ResponseEntity<FeedDetailResponseDto> updateFeed(@PathVariable Long id,
                                                            @RequestParam("feed") String feedStr,
                                                            @RequestParam(value = "files", required = false) List<MultipartFile> imageFiles) {

        FeedUpdateRequestDto requestDto = null;
        try {
            requestDto = new ObjectMapper().readValue(feedStr, FeedUpdateRequestDto.class);
        } catch (JsonProcessingException e) {
            log.info(feedStr);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "잘못된 형식의 입력입니다.", e);
        }
        String username = SecurityUtils.getCurrentUsername();

        FeedDetailResponseDto responseDto = feedService.updateFeed(username, id, requestDto, imageFiles == null ? Collections.emptyList() : imageFiles);

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    // 피드 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteFeed(@PathVariable Long id) {
        String username = SecurityUtils.getCurrentUsername();
        feedService.deleteFeed(username, id);

        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("message", "피드가 삭제되었습니다.");
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

    // 피드 좋아요
    @PostMapping("/{feedId}/like")
    public ResponseEntity<Map<String, String>> likeFeed(@PathVariable Long feedId) {
        String username = SecurityUtils.getCurrentUsername();
        String resultStr = feedService.likeFeed(username, feedId);

        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("message", resultStr);
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

    // 팔로워 피드 목록 조회
    @GetMapping("/followed")
    public ResponseEntity<List<FeedListResponseDto>> getFollowedUserFeeds() {
        String username = SecurityUtils.getCurrentUsername();
        List<FeedListResponseDto> followedUserFeeds = feedService.getFollowedUserFeeds(username);
        return ResponseEntity.ok(followedUserFeeds);
    }

    // 친구 피드 목록 조회
    @GetMapping("/friends")
    public ResponseEntity<List<FeedListResponseDto>> getFriendUserFeeds() {
        String username = SecurityUtils.getCurrentUsername();
        List<FeedListResponseDto> friendUserFeeds = feedService.getFriendUserFeeds(username);
        return ResponseEntity.ok(friendUserFeeds);
    }
}
