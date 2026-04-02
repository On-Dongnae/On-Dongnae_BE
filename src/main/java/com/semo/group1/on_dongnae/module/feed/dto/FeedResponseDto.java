package com.semo.group1.on_dongnae.module.feed.dto;

import com.semo.group1.on_dongnae.entity.Feed;
import com.semo.group1.on_dongnae.entity.FeedImage;
import com.semo.group1.on_dongnae.entity.enums.FeedGroup;
import com.semo.group1.on_dongnae.module.comment.dto.CommentResponseDto;
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
    private Integer commentCount;
    private List<String> imageUrls;
    private List<CommentResponseDto> comments;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static FeedResponseDto fromEntity(Feed feed) {
        List<CommentResponseDto> commentDtos = feed.getComments() != null ? 
                feed.getComments().stream()
                        .filter(c -> !c.getIsDeleted())
                        .map(CommentResponseDto::fromEntity)
                        .collect(Collectors.toList()) : null;

        return FeedResponseDto.builder()
                .id(feed.getId())
                .userId(feed.getUser() != null ? feed.getUser().getId() : null)
                .userEmail(feed.getUser() != null ? feed.getUser().getEmail() : null)
                .type(feed.getType())
                .title(feed.getTitle())
                .content(feed.getContent())
                .likeCount(feed.getLikeCount())
                .commentCount(commentDtos != null ? commentDtos.size() : 0)
                .imageUrls(feed.getImages() != null ? feed.getImages().stream()
                        .map(FeedImage::getImageUrl)
                        .collect(Collectors.toList()) : null)
                .comments(commentDtos)
                .createdAt(feed.getCreatedAt())
                .updatedAt(feed.getUpdatedAt())
                .build();
    }

    // 목록용 (댓글 리스트 제외)
    public static FeedResponseDto fromEntityForList(Feed feed) {
        long commentCount = feed.getComments() != null ? 
                feed.getComments().stream().filter(c -> !c.getIsDeleted()).count() : 0;

        return FeedResponseDto.builder()
                .id(feed.getId())
                .userId(feed.getUser() != null ? feed.getUser().getId() : null)
                .userEmail(feed.getUser() != null ? feed.getUser().getEmail() : null)
                .type(feed.getType())
                .title(feed.getTitle())
                .content(feed.getContent())
                .likeCount(feed.getLikeCount())
                .commentCount((int) commentCount)
                .imageUrls(feed.getImages() != null ? feed.getImages().stream()
                        .map(FeedImage::getImageUrl)
                        .collect(Collectors.toList()) : null)
                .createdAt(feed.getCreatedAt())
                .updatedAt(feed.getUpdatedAt())
                .build();
    }
}
