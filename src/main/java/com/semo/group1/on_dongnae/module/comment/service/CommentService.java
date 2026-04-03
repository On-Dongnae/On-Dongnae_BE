package com.semo.group1.on_dongnae.module.comment.service;

import com.semo.group1.on_dongnae.entity.Comment;
import com.semo.group1.on_dongnae.entity.Feed;
import com.semo.group1.on_dongnae.entity.User;
import com.semo.group1.on_dongnae.global.exception.CustomException;
import com.semo.group1.on_dongnae.global.exception.ErrorCode;
import com.semo.group1.on_dongnae.global.security.SecurityUtil;
import com.semo.group1.on_dongnae.module.comment.dto.CommentRequestDto;
import com.semo.group1.on_dongnae.module.comment.dto.CommentResponseDto;
import com.semo.group1.on_dongnae.module.comment.repository.CommentRepository;
import com.semo.group1.on_dongnae.module.feed.repository.FeedRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final FeedRepository feedRepository;
    private final SecurityUtil securityUtil;

    @Transactional
    public CommentResponseDto createComment(CommentRequestDto request) {
        User user = securityUtil.getCurrentUser();
        Feed feed = feedRepository.findById(request.getFeedId())
                .orElseThrow(() -> new CustomException(ErrorCode.FEED_NOT_FOUND));

        Comment comment = Comment.builder()
                .user(user)
                .feed(feed)
                .content(request.getContent())
                .build();

        return CommentResponseDto.fromEntity(commentRepository.save(comment));
    }

    @Transactional(readOnly = true)
    public List<CommentResponseDto> getComments(Long feedId) {
        return commentRepository.findByFeedIdAndIsDeletedFalseOrderByCreatedAtAsc(feedId)
                .stream()
                .map(CommentResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public CommentResponseDto updateComment(Long commentId, String content) {
        User user = securityUtil.getCurrentUser();
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomException(ErrorCode.COMMENT_NOT_FOUND));

        if (comment.getIsDeleted() || !comment.getUser().getId().equals(user.getId())) {
            throw new CustomException(ErrorCode.ACCESS_DENIED);
        }

        comment.update(content);
        return CommentResponseDto.fromEntity(comment);
    }

    @Transactional
    public void deleteComment(Long commentId) {
        User user = securityUtil.getCurrentUser();
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomException(ErrorCode.COMMENT_NOT_FOUND));

        if (comment.getIsDeleted() || !comment.getUser().getId().equals(user.getId())) {
            throw new CustomException(ErrorCode.ACCESS_DENIED);
        }

        comment.markAsDeleted();
    }

    @Transactional
    public CommentResponseDto plusLike(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomException(ErrorCode.COMMENT_NOT_FOUND));
        
        if (comment.getIsDeleted()) {
            throw new CustomException(ErrorCode.COMMENT_NOT_FOUND);
        }
        
        comment.plusLike();
        return CommentResponseDto.fromEntity(comment);
    }

    @Transactional
    public CommentResponseDto minusLike(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomException(ErrorCode.COMMENT_NOT_FOUND));

        if (comment.getIsDeleted()) {
            throw new CustomException(ErrorCode.COMMENT_NOT_FOUND);
        }

        comment.minusLike();
        return CommentResponseDto.fromEntity(comment);
    }
}
