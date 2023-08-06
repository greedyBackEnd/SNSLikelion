package com.example.likelionSNS.domain.service;

import com.example.likelionSNS.domain.dto.request.FeedRegisterRequestDto;
import com.example.likelionSNS.domain.dto.request.FeedUpdateRequestDto;
import com.example.likelionSNS.domain.dto.response.FeedDetailResponseDto;
import com.example.likelionSNS.domain.dto.response.FeedListResponseDto;
import com.example.likelionSNS.domain.entity.feed.Feed;
import com.example.likelionSNS.domain.entity.feed.FeedImages;
import com.example.likelionSNS.domain.entity.user.User;
import com.example.likelionSNS.domain.repository.FeedImagesRepository;
import com.example.likelionSNS.domain.repository.FeedRepository;
import com.example.likelionSNS.domain.repository.UserRepository;
import com.example.likelionSNS.utils.FileUploadUtils;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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
    public FeedDetailResponseDto registerFeed(String username, FeedRegisterRequestDto requestDto, List<MultipartFile> imageFiles, boolean isDraft) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("해당 유저를 찾을 수 없습니다." + username));

        Feed feedEntity = Feed.builder()
                .user(user)
                .title(requestDto.getTitle())
                .content(requestDto.getContent())
                .draft(isDraft)
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

        return FeedDetailResponseDto.of(feedEntity);
    }

    // 피드 단일 조회
    public FeedDetailResponseDto getFeed(Long id) {
        Feed feedEntity = feedRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("해당 피드를 찾을 수 없습니다."));
        List<String> imageUrls = feedEntity.getFeedImages().stream()
                .map(FeedImages::getImageUrl)
                .collect(Collectors.toList());

        return FeedDetailResponseDto.of(feedEntity, imageUrls);
    }

    // 피드 전체 조회 (특정 유저)
    public List<FeedListResponseDto> getUserFeeds(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("해당 유저를 찾을 수 없습니다." + username));
        return feedRepository.findByUserAndDraftFalse(user).stream()
                .map(feed -> {
                    String imageUrl = feed.getFeedImages().get(0).getImageUrl();
                    return FeedListResponseDto.of(feed, imageUrl);
                })
                .collect(Collectors.toList());
    }

    // 임시 저장된 피드 전체 조회
    public List<FeedListResponseDto> getUserDraftFeeds(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("해당 유저를 찾을 수 없습니다." + username));

        return feedRepository.findByUserAndDraftTrue(user).stream()
                .map(feed -> {
                    String imageUrl = feed.getFeedImages().get(0).getImageUrl();
                    return FeedListResponseDto.of(feed, imageUrl);
                })
                .collect(Collectors.toList());
    }

    // 피드 수정
    public FeedDetailResponseDto updateFeed(String username, Long id, FeedUpdateRequestDto requestDto, List<MultipartFile> imageFiles) {
        Feed feed = feedRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("해당 피드를 찾을 수 없습니다."));

        if (!feed.getUser().getUsername().equals(username)) {
            throw new IllegalArgumentException("피드를 수정할 권한이 없습니다.");
        }

        feed.update(requestDto.toEntity());
        feedRepository.save(feed);

        if (!imageFiles.isEmpty()) {
            // 기본 이미지 제거
            removeDefaultImage(feed);
            for (MultipartFile imageFile : imageFiles) {
                try {
                    String imageUrl = uploadImage(username, feed.getId(), imageFile);
                    FeedImages feedImage = FeedImages.builder()
                            .imageUrl(imageUrl)
                            .feed(feed)
                            .build();
                    feedImagesRepository.save(feedImage);
                } catch (IOException e) {
                    throw new IllegalArgumentException("이미지 파일 업로드에 실패하였습니다.");
                }
            }
        }

        feedRepository.save(feed);

        List<String> imageUrls = feed.getFeedImages().stream()
                .map(FeedImages::getImageUrl)
                .collect(Collectors.toList());

        return FeedDetailResponseDto.of(feed, imageUrls);
    }

    // 기본 이미지 설정
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

    // 기본 이미지 제거
    private void removeDefaultImage(Feed feed) {
        List<FeedImages> defaultImages = feed.getFeedImages().stream()
                .filter(image -> image.getImageUrl().equals(DEFAULT_IMAGE_URL))
                .collect(Collectors.toList());

        defaultImages.forEach(defaultImage -> {
            feed.getFeedImages().remove(defaultImage);
            feedImagesRepository.delete(defaultImage);
        });
    }
}
