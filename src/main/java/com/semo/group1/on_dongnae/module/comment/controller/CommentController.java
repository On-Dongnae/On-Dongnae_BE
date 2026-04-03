package com.semo.group1.on_dongnae.module.comment.controller;

import com.semo.group1.on_dongnae.global.common.ApiResponse;
import com.semo.group1.on_dongnae.module.comment.dto.CommentRequestDto;
import com.semo.group1.on_dongnae.module.comment.dto.CommentResponseDto;
import com.semo.group1.on_dongnae.module.comment.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Comment (댓글)", description = "피드 게시물에 달리는 댓글 관리")
@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @Operation(summary = "댓글 작성", description = "특정 피드에 댓글을 작성합니다.")
    @PostMapping
    public ResponseEntity<ApiResponse<CommentResponseDto>> createComment(@RequestBody CommentRequestDto request) {
        CommentResponseDto response = commentService.createComment(request);
        return ResponseEntity.ok(ApiResponse.created("댓글이 성공적으로 작성되었습니다.", response));
    }

    @Operation(summary = "댓글 목록 조회", description = "피드 ID를 기준으로 작성된 모든 댓글 목록을 조회합니다.")
    @GetMapping("/feed/{feedId}")
    public ResponseEntity<ApiResponse<List<CommentResponseDto>>> getComments(@PathVariable("feedId") Long feedId) {
        List<CommentResponseDto> response = commentService.getComments(feedId);
        return ResponseEntity.ok(ApiResponse.ok("댓글 목록 조회 성공", response));
    }

    @Operation(summary = "댓글 수정", description = "본인이 작성한 댓글의 내용을 수정합니다.")
    @PutMapping("/{commentId}")
    public ResponseEntity<ApiResponse<CommentResponseDto>> updateComment(
            @PathVariable("commentId") Long commentId,
            @RequestBody CommentRequestDto request) {
        CommentResponseDto response = commentService.updateComment(commentId, request.getContent());
        return ResponseEntity.ok(ApiResponse.ok("댓글이 수정되었습니다.", response));
    }

    @Operation(summary = "댓글 삭제", description = "댓글을 삭제합니다 (논리적 삭제).")
    @DeleteMapping("/{commentId}")
    public ResponseEntity<ApiResponse<Void>> deleteComment(@PathVariable("commentId") Long commentId) {
        commentService.deleteComment(commentId);
        return ResponseEntity.ok(ApiResponse.ok("댓글이 삭제되었습니다."));
    }

    @Operation(summary = "댓글 좋아요 추가", description = "댓글에 좋아요를 추가합니다.")
    @PostMapping("/{commentId}/like")
    public ResponseEntity<ApiResponse<CommentResponseDto>> plusLike(@PathVariable("commentId") Long commentId) {
        CommentResponseDto response = commentService.plusLike(commentId);
        return ResponseEntity.ok(ApiResponse.ok("댓글 좋아요 추가 성공", response));
    }

    @Operation(summary = "댓글 좋아요 취소", description = "댓글에 추가했던 좋아요를 취소합니다.")
    @PostMapping("/{commentId}/unlike")
    public ResponseEntity<ApiResponse<CommentResponseDto>> minusLike(@PathVariable("commentId") Long commentId) {
        CommentResponseDto response = commentService.minusLike(commentId);
        return ResponseEntity.ok(ApiResponse.ok("댓글 좋아요 취소 성공", response));
    }
}
