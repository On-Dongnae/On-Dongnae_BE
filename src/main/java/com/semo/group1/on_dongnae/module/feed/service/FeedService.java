package com.semo.group1.on_dongnae.module.feed.service;

import com.semo.group1.on_dongnae.entity.Feed;
import com.semo.group1.on_dongnae.entity.FeedImage;
import com.semo.group1.on_dongnae.entity.User;
import com.semo.group1.on_dongnae.global.aws.S3UploadService;
import com.semo.group1.on_dongnae.global.exception.CustomException;
import com.semo.group1.on_dongnae.global.exception.ErrorCode;
import com.semo.group1.on_dongnae.global.security.SecurityUtil;
import com.semo.group1.on_dongnae.module.feed.dto.FeedRequestDto;
import com.semo.group1.on_dongnae.module.feed.dto.FeedResponseDto;
import com.semo.group1.on_dongnae.module.feed.repository.FeedRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class FeedService {

    private final FeedRepository feedRepository;
    private final S3UploadService s3UploadService;
    private final SecurityUtil securityUtil;

    @Transactional
    public FeedResponseDto createFeed(FeedRequestDto request, List<MultipartFile> images) {
        User user = securityUtil.getCurrentUser();

        Feed feed = Feed.builder()
                .user(user)
                .type(request.getType())
                .title(request.getTitle())
                .content(request.getContent())
                .images(new ArrayList<>())
                .build();

        Feed savedFeed = feedRepository.save(feed);

        if (images != null && !images.isEmpty()) {
            int order = 1;
            for (MultipartFile file : images) {
                if (file.isEmpty()) continue;
                try {
                    String imageUrl = s3UploadService.uploadFile(file, "feeds/" + savedFeed.getId());
                    FeedImage fi = FeedImage.builder()
                            .feed(savedFeed)
                            .imageUrl(imageUrl)
                            .displayOrder(order++)
                            .build();
                    savedFeed.getImages().add(fi);
                } catch (IOException e) {
                    log.error("S3 파일 업로드 중 오류 발생", e);
                    throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
                }
            }
        }

        return FeedResponseDto.fromEntity(savedFeed);
    }

    @Transactional(readOnly = true)
    public List<FeedResponseDto> getFeeds() {
        return feedRepository.findByIsDeletedFalseOrderByCreatedAtDesc()
                .stream()
                .map(FeedResponseDto::fromEntityForList)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public FeedResponseDto getFeed(Long feedId) {
        Feed feed = feedRepository.findById(feedId)
                .orElseThrow(() -> new CustomException(ErrorCode.FEED_NOT_FOUND));
        
        if (feed.getIsDeleted()) {
            throw new CustomException(ErrorCode.FEED_NOT_FOUND);
        }
        
        return FeedResponseDto.fromEntity(feed);
    }

    @Transactional
    public FeedResponseDto updateFeed(Long feedId, FeedRequestDto request, List<MultipartFile> addImages) {
        User user = securityUtil.getCurrentUser();
        
        Feed feed = feedRepository.findById(feedId)
                .orElseThrow(() -> new CustomException(ErrorCode.FEED_NOT_FOUND));

        if (feed.getIsDeleted() || !feed.getUser().getId().equals(user.getId())) {
            throw new CustomException(ErrorCode.ACCESS_DENIED);
        }

        feed.update(request.getTitle(), request.getContent(), request.getType());

        if (addImages != null && !addImages.isEmpty()) {
            int order = feed.getImages().size() + 1;
            for (MultipartFile file : addImages) {
                if (file.isEmpty()) continue;
                try {
                    String imageUrl = s3UploadService.uploadFile(file, "feeds/" + feed.getId());
                    FeedImage fi = FeedImage.builder()
                            .feed(feed)
                            .imageUrl(imageUrl)
                            .displayOrder(order++)
                            .build();
                    feed.getImages().add(fi);
                } catch (IOException e) {
                    log.error("S3 파일 업로드 중 오류 발생", e);
                    throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
                }
            }
        }

        return FeedResponseDto.fromEntity(feed);
    }

    @Transactional
    public void deleteFeed(Long feedId) {
        User user = securityUtil.getCurrentUser();
        
        Feed feed = feedRepository.findById(feedId)
                .orElseThrow(() -> new CustomException(ErrorCode.FEED_NOT_FOUND));

        if (feed.getIsDeleted() || !feed.getUser().getId().equals(user.getId())) {
            throw new CustomException(ErrorCode.ACCESS_DENIED);
        }

        feed.markAsDeleted();
    }

    @Transactional
    public FeedResponseDto plusLike(Long feedId) {
        Feed feed = feedRepository.findById(feedId)
                .orElseThrow(() -> new CustomException(ErrorCode.FEED_NOT_FOUND));

        if (feed.getIsDeleted()) {
            throw new CustomException(ErrorCode.FEED_NOT_FOUND);
        }

        feed.plusLike();
        return FeedResponseDto.fromEntity(feed);
    }

    @Transactional
    public FeedResponseDto minusLike(Long feedId) {
        Feed feed = feedRepository.findById(feedId)
                .orElseThrow(() -> new CustomException(ErrorCode.FEED_NOT_FOUND));

        if (feed.getIsDeleted()) {
            throw new CustomException(ErrorCode.FEED_NOT_FOUND);
        }

        feed.minusLike();
        return FeedResponseDto.fromEntity(feed);
    }

    @Transactional(readOnly = true)
    public List<FeedResponseDto> getMyFeeds() {
        User user = securityUtil.getCurrentUser();
        return feedRepository.findByUserAndIsDeletedFalseOrderByCreatedAtDesc(user)
                .stream()
                .map(FeedResponseDto::fromEntityForList)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<FeedResponseDto> getMyCommentedFeeds() {
        User user = securityUtil.getCurrentUser();
        return feedRepository.findMyCommentedFeeds(user)
                .stream()
                .map(FeedResponseDto::fromEntityForList)
                .collect(Collectors.toList());
    }
}
