package com.semo.group1.on_dongnae.module.comment.controller;

import com.semo.group1.on_dongnae.global.common.ApiResponse;
import com.semo.group1.on_dongnae.module.comment.dto.CommentRequestDto;
import com.semo.group1.on_dongnae.module.comment.dto.CommentResponseDto;
import com.semo.group1.on_dongnae.module.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<ApiResponse<CommentResponseDto>> createComment(@RequestBody CommentRequestDto request) {
        CommentResponseDto response = commentService.createComment(request);
        return ResponseEntity.ok(ApiResponse.created("댓글이 성공적으로 작성되었습니다.", response));
    }

    @GetMapping("/feed/{feedId}")
    public ResponseEntity<ApiResponse<List<CommentResponseDto>>> getComments(@PathVariable("feedId") Long feedId) {
        List<CommentResponseDto> response = commentService.getComments(feedId);
        return ResponseEntity.ok(ApiResponse.ok("댓글 목록 조회 성공", response));
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<ApiResponse<CommentResponseDto>> updateComment(
            @PathVariable("commentId") Long commentId,
            @RequestBody CommentRequestDto request) {
        CommentResponseDto response = commentService.updateComment(commentId, request.getContent());
        return ResponseEntity.ok(ApiResponse.ok("댓글이 수정되었습니다.", response));
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<ApiResponse<Void>> deleteComment(@PathVariable("commentId") Long commentId) {
        commentService.deleteComment(commentId);
        return ResponseEntity.ok(ApiResponse.ok("댓글이 삭제되었습니다."));
    }

    @PostMapping("/{commentId}/like")
    public ResponseEntity<ApiResponse<CommentResponseDto>> plusLike(@PathVariable("commentId") Long commentId) {
        CommentResponseDto response = commentService.plusLike(commentId);
        return ResponseEntity.ok(ApiResponse.ok("댓글 좋아요 추가 성공", response));
    }

    @PostMapping("/{commentId}/unlike")
    public ResponseEntity<ApiResponse<CommentResponseDto>> minusLike(@PathVariable("commentId") Long commentId) {
        CommentResponseDto response = commentService.minusLike(commentId);
        return ResponseEntity.ok(ApiResponse.ok("댓글 좋아요 취소 성공", response));
    }
}
