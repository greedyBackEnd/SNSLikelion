package com.example.likelionSNS.domain.controller;

import com.example.likelionSNS.domain.dto.request.UserUpdateDto;
import com.example.likelionSNS.domain.dto.response.FollowResponseDto;
import com.example.likelionSNS.domain.dto.response.FriendRequestResponseDto;
import com.example.likelionSNS.domain.dto.response.UserResponseDto;
import com.example.likelionSNS.domain.service.UserService;
import com.example.likelionSNS.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    // 유저 정보 조회
    @GetMapping("/profile")
    private ResponseEntity<UserResponseDto> getProfile() {
        String username = SecurityUtils.getCurrentUsername();
        UserResponseDto userProfile = userService.getUserProfile(username);

        return new ResponseEntity<>(userProfile, HttpStatus.OK);
    }

    // 유저 정보 수정
    @PutMapping("/profile")
    private ResponseEntity<Map<String, String>> updateProfile(@RequestBody UserUpdateDto userUpdateDto) {
        String username = SecurityUtils.getCurrentUsername();
        userService.updateUser(username, userUpdateDto);

        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("message", "유저 정보가 수정되었습니다.");
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

    // 프로필 사진 업로드
    @PostMapping("/profile")
    private ResponseEntity<Map<String, String>> uploadProfileImg(@RequestParam("image") MultipartFile imageFile) {
        String username = SecurityUtils.getCurrentUsername();
        userService.uploadProfileImg(username, imageFile);

        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("message", "프로필 이미지가 업로드되었습니다.");
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

    // 유저 팔로우
    @PostMapping("/follow/{targetUserId}")
    public ResponseEntity<Map<String, String>> followUser(@PathVariable Long targetUserId) {
        String username = SecurityUtils.getCurrentUsername();
        userService.follow(username, targetUserId);

        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("message", "팔로우되었습니다.");
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

    // 유저 언팔로우
    @PostMapping("/unfollow/{targetUserId}")
    public ResponseEntity<Map<String, String>> unfollowUser(@PathVariable Long targetUserId) {
        String username = SecurityUtils.getCurrentUsername();
        userService.unfollow(username, targetUserId);

        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("message", "팔로우가 취소되었습니다.");
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

    // 팔로우 목록 반환
    @GetMapping("/followers")
    public ResponseEntity<List<FollowResponseDto>> getFollowings() {
        String username = SecurityUtils.getCurrentUsername();

        List<FollowResponseDto> followers = userService.getFollowers(username);
        return new ResponseEntity<>(followers, HttpStatus.OK);
    }

    @PostMapping("/friend/request/{targetUserId}")
    public ResponseEntity<Map<String, String>> sendFriendRequest(@PathVariable Long targetUserId) {
        String username = SecurityUtils.getCurrentUsername();
        userService.sendFriendRequest(username, targetUserId);

        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("message", "친구 요청을 보냈습니다.");
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

    @PostMapping("/friend/accept/{requestId}")
    public ResponseEntity<Map<String, String>> acceptFriendRequest(@PathVariable Long requestId) {
        userService.acceptFriendRequest(requestId);

        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("message", "친구 요청을 수락하였습니다.");
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

    @PostMapping("/friend/reject/{requestId}")
    public ResponseEntity<Map<String, String>> rejectFriendRequest(@PathVariable Long requestId) {
        userService.rejectFriendRequest(requestId);

        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("message", "친구 요청을 거절하였습니다.");
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

    @GetMapping("/friend-requests")
    private ResponseEntity<List<FriendRequestResponseDto>> getFriendRequests() {
        String username = SecurityUtils.getCurrentUsername();
        List<FriendRequestResponseDto> responseDtos = userService.getFriendRequests(username);
        return new ResponseEntity<>(responseDtos, HttpStatus.OK);
    }
}
