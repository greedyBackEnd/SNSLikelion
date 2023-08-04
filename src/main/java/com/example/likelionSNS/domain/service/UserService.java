package com.example.likelionSNS.domain.service;

import com.example.likelionSNS.domain.dto.request.UserUpdateDto;
import com.example.likelionSNS.domain.dto.response.UserResponseDto;
import com.example.likelionSNS.domain.entity.user.User;
import com.example.likelionSNS.domain.repository.UserRepository;
import com.example.likelionSNS.utils.FileUploadUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    // 유저 정보 조회
    public UserResponseDto getUserProfile(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("해당 유저를 찾을 수 없습니다." + username));
        return UserResponseDto.of(user);
    }

    // 유저 정보 수정
    @Transactional
    public void updateUser(String username, UserUpdateDto updateDto) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("해당 유저를 찾을 수 없습니다." + username));
        user.updateUser(updateDto.toEntity());
        userRepository.save(user);
    }

    @Transactional
    public void uploadProfileImg(String username, MultipartFile imageFile) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("해당 유저를 찾을 수 없습니다." + username));

        String originalFileName = StringUtils.cleanPath(Objects.requireNonNull(imageFile.getOriginalFilename()));
        String uploadDir = "static/images/userProfileImg/" + username;

        try {
            String uploadPath = FileUploadUtils.saveFile(uploadDir, originalFileName, imageFile);
            user.updateProfileImg(uploadPath);
        } catch (IOException e) {
            throw new IllegalArgumentException("이미지 파일 업로드에 실패하였습니다.");
        }
    }
}
