package com.semo.group1.on_dongnae.module.comment.dto;

import com.semo.group1.on_dongnae.entity.Comment;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class CommentResponseDto {
    private Long id;
    private Long userId;
    private String userEmail;
    private String content;
    private Integer likeCount;
    private LocalDateTime createdAt;

    public static CommentResponseDto fromEntity(Comment comment) {
        return CommentResponseDto.builder()
                .id(comment.getId())
                .userId(comment.getUser().getId())
                .userEmail(comment.getUser().getEmail())
                .content(comment.getContent())
                .likeCount(comment.getLikeCount())
                .createdAt(comment.getCreatedAt())
                .build();
    }
}
