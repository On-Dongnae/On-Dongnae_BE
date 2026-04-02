package com.semo.group1.on_dongnae.module.feed.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.semo.group1.on_dongnae.entity.enums.FeedGroup;
import com.semo.group1.on_dongnae.entity.enums.FeedSort;
import com.semo.group1.on_dongnae.global.common.ApiResponse;
import com.semo.group1.on_dongnae.global.exception.CustomException;
import com.semo.group1.on_dongnae.global.exception.ErrorCode;
import com.semo.group1.on_dongnae.module.feed.dto.FeedRequestDto;
import com.semo.group1.on_dongnae.module.feed.dto.FeedResponseDto;
import com.semo.group1.on_dongnae.module.feed.service.FeedService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Tag(name = "Feed (피드)", description = "게시글 작성, 조회, 추천 등 피드 관련 API")
@Slf4j
@RestController
@RequestMapping("/api/feeds")
@RequiredArgsConstructor
public class FeedController {

    private final FeedService feedService;

    // 1. 피드 생성
    // FeedController.java

    @Operation(summary = "피드 작성", description = "피드 내용과 이미지를 함께 업로드합니다.")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<FeedResponseDto>> createFeed(
            // DTO로 직접 받습니다!
            @RequestPart("request") FeedRequestDto request,
            @RequestPart(value = "images", required = false) List<MultipartFile> images) {

        // 번거로운 ObjectMapper 변환 과정 없이 바로 서비스 호출!
        FeedResponseDto response = feedService.createFeed(request, images);
        return ResponseEntity.ok(ApiResponse.created("피드가 성공적으로 작성되었습니다.", response));
    }

    @Operation(summary = "피드 목록 조회", description = "전체 피드 목록을 조회하며, 카테고리 필터링 및 정렬 기능을 지원합니다.")
    @GetMapping
    public ResponseEntity<ApiResponse<List<FeedResponseDto>>> getFeeds(
            @Parameter(description = "필터링할 피드 그룹 (RECORDS, CHATS 등)") @RequestParam(value = "type", required = false) FeedGroup type,
            @Parameter(description = "정렬 기준 (LATEST, LIKES)") @RequestParam(value = "sortBy", defaultValue = "LATEST") FeedSort sortBy) {
        List<FeedResponseDto> response = feedService.getFeeds(type, sortBy);
        return ResponseEntity.ok(ApiResponse.ok("피드 목록 조회 성공", response));
    }

    @Operation(summary = "피드 상세 조회", description = "ID를 통해 특정 피드의 상세 내용을 조회합니다.")
    @GetMapping("/{feedId}")
    public ResponseEntity<ApiResponse<FeedResponseDto>> getFeed(@Parameter(description = "조회할 피드 ID") @PathVariable("feedId") Long feedId) {
        FeedResponseDto response = feedService.getFeed(feedId);
        return ResponseEntity.ok(ApiResponse.ok("피드 상세 조회 성공", response));
    }

    @Operation(summary = "내가 작성한 피드 조회", description = "현재 사용자가 작성한 모든 피드 목록을 조회합니다.")
    @GetMapping("/my")
    public ResponseEntity<ApiResponse<List<FeedResponseDto>>> getMyFeeds() {
        List<FeedResponseDto> response = feedService.getMyFeeds();
        return ResponseEntity.ok(ApiResponse.ok("내가 작성한 피드 조회 성공", response));
    }

    @Operation(summary = "활동 피드 조회 (댓글 작성 기준)", description = "내가 댓글을 남겼던 피드 목록을 조회합니다.")
    @GetMapping("/my-comments")
    public ResponseEntity<ApiResponse<List<FeedResponseDto>>> getMyCommentedFeeds() {
        List<FeedResponseDto> response = feedService.getMyCommentedFeeds();
        return ResponseEntity.ok(ApiResponse.ok("내가 댓글을 작성한 피드 조회 성공", response));
    }

    @Operation(summary = "피드 수정", description = "기존 피드의 내용을 수정하고 새 이미지를 추가합니다.")
    @PutMapping(value = "/{feedId}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<ApiResponse<FeedResponseDto>> updateFeed(
            @Parameter(description = "수정할 피드 ID") @PathVariable("feedId") Long feedId,
            @RequestPart("request") FeedRequestDto request,
            @RequestPart(value = "addImages", required = false) List<MultipartFile> addImages) {

        FeedResponseDto response = feedService.updateFeed(feedId, request, addImages);
        return ResponseEntity.ok(ApiResponse.ok("피드가 성공적으로 수정되었습니다.", response));
    }

    @Operation(summary = "피드 삭제", description = "피드를 삭제합니다. 실제 DB에서 지우지 않고 논리적으로 삭제 처리합니다.")
    @DeleteMapping("/{feedId}")
    public ResponseEntity<ApiResponse<Void>> deleteFeed(@Parameter(description = "삭제할 피드 ID") @PathVariable("feedId") Long feedId) {
        feedService.deleteFeed(feedId);
        return ResponseEntity.ok(ApiResponse.ok("피드가 삭제되었습니다."));
    }

    @Operation(summary = "피드 좋아요 추가", description = "특정 피드에 좋아요를 클릭합니다.")
    @PostMapping("/{feedId}/like")
    public ResponseEntity<ApiResponse<FeedResponseDto>> plusLike(@Parameter(description = "좋아요를 누를 피드 ID") @PathVariable("feedId") Long feedId) {
        FeedResponseDto response = feedService.plusLike(feedId);
        return ResponseEntity.ok(ApiResponse.ok("피드 좋아요 추가 성공", response));
    }

    @Operation(summary = "피드 좋아요 취소", description = "피드에 눌렀던 좋아요를 취소합니다.")
    @PostMapping("/{feedId}/unlike")
    public ResponseEntity<ApiResponse<FeedResponseDto>> minusLike(@Parameter(description = "좋아요를 취소할 피드 ID") @PathVariable("feedId") Long feedId) {
        FeedResponseDto response = feedService.minusLike(feedId);
        return ResponseEntity.ok(ApiResponse.ok("피드 좋아요 취소 성공", response));
    }
}
