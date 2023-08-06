package com.example.likelionSNS.domain.controller;


import com.example.likelionSNS.domain.dto.request.FeedRegisterRequestDto;
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

//    @PostMapping
//    public ResponseEntity<Map<String, String>> registerFeed(@RequestBody FeedRegisterRequestDto requestDto,
//                                                            @RequestPart(value = "files", required = false) Optional<List<MultipartFile>> optionalMultipartFiles) {
//        String username = SecurityUtils.getCurrentUsername();
//        List<MultipartFile> imageFiles = optionalMultipartFiles.orElse(Collections.emptyList());
//        feedService.registerFeed(username, requestDto, imageFiles);
//
//        Map<String, String> responseBody = new HashMap<>();
//        responseBody.put("message", "피드가 등록되었습니다.");
//        return new ResponseEntity<>(responseBody, HttpStatus.OK);
//    }

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

        feedService.registerFeed(username, requestDto, imageFiles == null ? Collections.emptyList() : imageFiles);

        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("message", "피드가 등록되었습니다.");
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }
}
