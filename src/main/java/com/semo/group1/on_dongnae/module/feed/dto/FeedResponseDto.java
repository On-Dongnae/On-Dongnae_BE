package com.semo.group1.on_dongnae.module.feed.dto;

import com.semo.group1.on_dongnae.entity.Feed;
import com.semo.group1.on_dongnae.entity.enums.FeedGroup;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class FeedResponseDto {
    private Long id;
    private Long userId;
    private String userEmail;
    private FeedGroup type;
    private String title;
    private String content;
    private Integer likeCount;
    private List<String> imageUrls;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static FeedResponseDto fromEntity(Feed feed) {
        return FeedResponseDto.builder()
                .id(feed.getId())
                .userId(feed.getUser() != null ? feed.getUser().getId() : null)
                .userEmail(feed.getUser() != null ? feed.getUser().getEmail() : null)
                .type(feed.getType())
                .title(feed.getTitle())
                .content(feed.getContent())
                .likeCount(feed.getLikeCount())
                .imageUrls(feed.getImages() != null ? feed.getImages().stream()
                        .map(img -> img.getImageUrl())
                        .collect(Collectors.toList()) : null)
                .createdAt(feed.getCreatedAt())
                .updatedAt(feed.getUpdatedAt())
                .build();
    }
}
