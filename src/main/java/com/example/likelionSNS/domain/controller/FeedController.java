package com.example.likelionSNS.domain.controller;


import com.example.likelionSNS.domain.dto.request.FeedRegisterRequestDto;
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
                                                         @RequestParam(value="files", required=false) List<MultipartFile> imageFiles){
        FeedRegisterRequestDto requestDto = null;
        try {
            requestDto = new ObjectMapper().readValue(feedStr, FeedRegisterRequestDto.class);
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
                                                            @RequestParam(value="files", required=false) List<MultipartFile> imageFiles){
        FeedRegisterRequestDto requestDto = null;
        try {
            requestDto = new ObjectMapper().readValue(feedStr, FeedRegisterRequestDto.class);
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
        log.info(String.valueOf(feedService.getFeed(id)));
        return new ResponseEntity<>(feedService.getFeed(id), HttpStatus.OK);
    }

    // 특정 유저 피드 조회
    @GetMapping("/user/{username}")
    public ResponseEntity<List<FeedListResponseDto>> getUserFeeds(@PathVariable String username) {
        return new ResponseEntity<>(feedService.getUserFeeds(username), HttpStatus.OK);
    }
}
