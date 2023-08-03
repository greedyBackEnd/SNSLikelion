package com.example.likelionSNS.domain.controller;

import com.example.likelionSNS.domain.dto.request.UserUpdateDto;
import com.example.likelionSNS.domain.dto.response.UserResponseDto;
import com.example.likelionSNS.domain.service.UserService;
import com.example.likelionSNS.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
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
}
