package com.example.likelionSNS.domain.service;

import com.example.likelionSNS.domain.dto.request.FeedRegisterRequestDto;
import com.example.likelionSNS.domain.dto.response.FeedResponseDto;
import com.example.likelionSNS.domain.entity.feed.Feed;
import com.example.likelionSNS.domain.entity.feed.FeedImages;
import com.example.likelionSNS.domain.entity.user.User;
import com.example.likelionSNS.domain.repository.FeedImagesRepository;
import com.example.likelionSNS.domain.repository.FeedRepository;
import com.example.likelionSNS.domain.repository.UserRepository;
import com.example.likelionSNS.utils.FileUploadUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class FeedService {

    // 기본 이미지 경로
    private static final String DEFAULT_IMAGE_URL = "static/images/defaultImg/defaultFeedImg.jpg";
    // 새로운 이미지가 저장될 경로
    private static final String UPLOAD_DIR_FORMAT = "static/images/feedImg/%s/%d";

    private final FeedRepository feedRepository;
    private final UserRepository userRepository;
    private final FeedImagesRepository feedImagesRepository;

    // 피드 등록
    public FeedResponseDto registerFeed(String username, FeedRegisterRequestDto requestDto, List<MultipartFile> imageFiles) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("해당 유저를 찾을 수 없습니다." + username));

        Feed feedEntity = Feed.builder()
                .user(user)
                .title(requestDto.getTitle())
                .content(requestDto.getContent())
                .draft(false)
                .build();

        feedEntity = feedRepository.save(feedEntity);

        if (imageFiles.isEmpty()) {
            attachDefaultImage(feedEntity);
        } else {
            for (MultipartFile imageFile : imageFiles) {
                try {
                    String imageUrl = uploadImage(username, feedEntity.getId(), imageFile);
                    FeedImages feedImage = FeedImages.builder()
                            .imageUrl(imageUrl)
                            .feed(feedEntity)
                            .build();
                    feedImagesRepository.save(feedImage);
                } catch (IOException e) {
                    throw new IllegalArgumentException("이미지 파일 업로드에 실패하였습니다.");
                }
            }
        }

        return FeedResponseDto.of(feedEntity);
    }

    // 기본 이미지 업로드
    private void attachDefaultImage(Feed feedEntity) {
        FeedImages defaultFeedImage = FeedImages.builder()
                .imageUrl(DEFAULT_IMAGE_URL)
                .feed(feedEntity)
                .build();
        feedImagesRepository.save(defaultFeedImage);
    }

    // 사용자 지정 이미지 업로드
    private String uploadImage(String username, Long feedId, MultipartFile imageFile) throws IOException {
        String originalFileName = StringUtils.cleanPath(Objects.requireNonNull(imageFile.getOriginalFilename()));
        String uploadDir = String.format(UPLOAD_DIR_FORMAT, username, feedId);
        return FileUploadUtils.saveFile(uploadDir, originalFileName, imageFile);
    }
}
