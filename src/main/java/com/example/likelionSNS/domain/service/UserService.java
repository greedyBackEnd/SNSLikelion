package com.example.likelionSNS.domain.service;

import com.example.likelionSNS.domain.dto.request.UserUpdateDto;
import com.example.likelionSNS.domain.dto.response.FollowResponseDto;
import com.example.likelionSNS.domain.dto.response.FriendRequestResponseDto;
import com.example.likelionSNS.domain.dto.response.UserResponseDto;
import com.example.likelionSNS.domain.entity.user.FriendRequest;
import com.example.likelionSNS.domain.entity.user.User;
import com.example.likelionSNS.domain.repository.FriendRequestRepository;
import com.example.likelionSNS.domain.repository.UserRepository;
import com.example.likelionSNS.utils.FileUploadUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final FriendRequestRepository friendRequestRepository;

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

    @Transactional
    public void follow(String username, Long targetUserId) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("해당 유저를 찾을 수 없습니다." + username));

        if (user.getId().equals(targetUserId)) {
            throw new IllegalArgumentException("자신을 팔로우할 수 없습니다.");
        }

        User targetUser = userRepository.findById(targetUserId)
                .orElseThrow(() -> new UsernameNotFoundException("해당 유저를 찾을 수 없습니다." + username));
        user.follow(targetUser);
    }

    @Transactional
    public void unfollow(String username, Long targetUserId) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("해당 유저를 찾을 수 없습니다." + username));

        User targetUser = userRepository.findById(targetUserId)
                .orElseThrow(() -> new UsernameNotFoundException("해당 유저를 찾을 수 없습니다." + username));
        user.unfollow(targetUser);
    }

    public List<FollowResponseDto> getFollowers(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("해당 유저를 찾을 수 없습니다." + username));

        return user.getFollowers().stream()
                .map(FollowResponseDto::of)
                .collect(Collectors.toList());
    }

    @Transactional
    public void sendFriendRequest(String username, Long targetUserId) {
        User requester = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("해당 유저를 찾을 수 없습니다." + username));

        User receiver = userRepository.findById(targetUserId)
                .orElseThrow(() -> new UsernameNotFoundException("해당 유저를 찾을 수 없습니다." + targetUserId));

        if (requester.equals(receiver)) {
            throw new IllegalArgumentException("자기 자신에게 친구 요청을 할 수 없습니다.");
        }

        // 이미 친구인 경우에 대한 검증
        if (requester.getFriends().contains(receiver) || receiver.getFriends().contains(requester)) {
            throw new IllegalArgumentException("이미 친구인 사용자에게는 친구 요청을 할 수 없습니다.");
        }

        FriendRequest friendRequest = FriendRequest.builder()
                .requester(requester)
                .receiver(receiver)
                .build();

        friendRequestRepository.save(friendRequest);
    }

    @Transactional
    public void acceptFriendRequest(Long requestId) {
        FriendRequest request = friendRequestRepository.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("해당 친구 요청을 찾을 수 없습니다."));

        request.getRequester().addFriend(request.getReceiver());
        friendRequestRepository.delete(request);
    }

    @Transactional
    public void rejectFriendRequest(Long requestId) {
        FriendRequest request = friendRequestRepository.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("해당 친구 요청을 찾을 수 없습니다."));
        friendRequestRepository.delete(request);
    }

    public List<FriendRequestResponseDto> getFriendRequests(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("해당 유저를 찾을 수 없습니다." + username));

        return friendRequestRepository.findByReceiver(user).stream()
                .map(FriendRequestResponseDto::of)
                .collect(Collectors.toList());
    }

}
