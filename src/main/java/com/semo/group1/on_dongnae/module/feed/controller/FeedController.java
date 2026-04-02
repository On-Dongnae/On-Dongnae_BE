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
import io.swagger.v3.oas.annotations.media.Content;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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

    // 2. 전체 피드 목록 조회 (단순 List)
    @GetMapping
    public ResponseEntity<ApiResponse<List<FeedResponseDto>>> getFeeds(
            @RequestParam(value = "type", required = false) FeedGroup type,
            @RequestParam(value = "sortBy", defaultValue = "LATEST") FeedSort sortBy) {
        List<FeedResponseDto> response = feedService.getFeeds(type, sortBy);
        return ResponseEntity.ok(ApiResponse.ok("피드 목록 조회 성공", response));
    }

    // 3. 특정 피드 상세 조회
    @GetMapping("/{feedId}")
    public ResponseEntity<ApiResponse<FeedResponseDto>> getFeed(@PathVariable("feedId") Long feedId) {
        FeedResponseDto response = feedService.getFeed(feedId);
        return ResponseEntity.ok(ApiResponse.ok("피드 상세 조회 성공", response));
    }

    @GetMapping("/my")
    public ResponseEntity<ApiResponse<List<FeedResponseDto>>> getMyFeeds() {
        List<FeedResponseDto> response = feedService.getMyFeeds();
        return ResponseEntity.ok(ApiResponse.ok("내가 작성한 피드 조회 성공", response));
    }

    @GetMapping("/my-comments")
    public ResponseEntity<ApiResponse<List<FeedResponseDto>>> getMyCommentedFeeds() {
        List<FeedResponseDto> response = feedService.getMyCommentedFeeds();
        return ResponseEntity.ok(ApiResponse.ok("내가 댓글을 작성한 피드 조회 성공", response));
    }

    // 4. 피드 수정
    @PutMapping(value = "/{feedId}", consumes = { "multipart/form-data" })
    public ResponseEntity<ApiResponse<FeedResponseDto>> updateFeed(
            @PathVariable("feedId") Long feedId,
            @RequestPart("request") FeedRequestDto request,
            @RequestPart(value = "addImages", required = false) List<MultipartFile> addImages) {

        FeedResponseDto response = feedService.updateFeed(feedId, request, addImages);
        return ResponseEntity.ok(ApiResponse.ok("피드가 성공적으로 수정되었습니다.", response));
    }

    // 5. 피드 삭제 (논리적 삭제)
    @DeleteMapping("/{feedId}")
    public ResponseEntity<ApiResponse<Void>> deleteFeed(@PathVariable("feedId") Long feedId) {
        feedService.deleteFeed(feedId);
        return ResponseEntity.ok(ApiResponse.ok("피드가 삭제되었습니다."));
    }

    // 6. 피드 좋아요 추가
    @PostMapping("/{feedId}/like")
    public ResponseEntity<ApiResponse<FeedResponseDto>> plusLike(@PathVariable("feedId") Long feedId) {
        FeedResponseDto response = feedService.plusLike(feedId);
        return ResponseEntity.ok(ApiResponse.ok("피드 좋아요 추가 성공", response));
    }

    // 7. 피드 좋아요 취소
    @PostMapping("/{feedId}/unlike")
    public ResponseEntity<ApiResponse<FeedResponseDto>> minusLike(@PathVariable("feedId") Long feedId) {
        FeedResponseDto response = feedService.minusLike(feedId);
        return ResponseEntity.ok(ApiResponse.ok("피드 좋아요 취소 성공", response));
    }
}
