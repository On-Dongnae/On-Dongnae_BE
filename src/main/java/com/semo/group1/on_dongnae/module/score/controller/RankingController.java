package com.semo.group1.on_dongnae.module.score.controller;

import com.semo.group1.on_dongnae.global.common.ApiResponse;
import com.semo.group1.on_dongnae.global.security.SecurityUtil;
import com.semo.group1.on_dongnae.module.score.dto.MyScoreResponseDto;
import com.semo.group1.on_dongnae.module.score.dto.RegionRanking;
import com.semo.group1.on_dongnae.module.score.dto.UserRanking;
import com.semo.group1.on_dongnae.module.score.service.ScoreService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Ranking (랭킹)", description = "전체/지역별 랭킹 및 내 점수 조회")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/rankings")
public class RankingController {
    private final ScoreService scoreService;
    private final SecurityUtil securityUtil;

    @Operation(summary = "전체 유저 랭킹 조회", description = "서비스 전체 유저 중 상위 N명의 랭킹을 조회합니다.")
    @GetMapping("/user")
    public ResponseEntity<List<UserRanking>> getUserRankings(
            @RequestParam(defaultValue = "10") int n) {
        return ResponseEntity.ok(scoreService.getTopNUserRanking(n));
    }

    @Operation(summary = "지역별 랭킹 조회", description = "동네별 점수 합산 기준 상위 N개의 지역 랭킹을 조회합니다.")
    @GetMapping("/region")
    public ResponseEntity<List<RegionRanking>> getRegionRankings(
            @RequestParam(defaultValue = "10") int n) {
        return ResponseEntity.ok(scoreService.getTopNRegionRanking(n));
    }

    @Operation(summary = "내 랭킹 조회", description = "현재 로그인한 유저의 전체 랭킹 등수를 조회합니다.")
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<Long>> getMyRanking() {
        Long userId = securityUtil.getCurrentUser().getId();
        Long myRank = scoreService.getMyRanking(userId);
        return ResponseEntity.ok(ApiResponse.ok("내 등수 조회 성공", myRank));
    }

    @Operation(summary = "내 점수 및 이력 조회", description = "현재 로그인한 유저의 총 점수와 상세 적립/차감 이력을 조회합니다.")
    @GetMapping("/my-score")
    public ResponseEntity<ApiResponse<MyScoreResponseDto>> getMyScore() {
        Long userId = securityUtil.getCurrentUser().getId();
        MyScoreResponseDto response = scoreService.getMyScoreHistory(userId);
        return ResponseEntity.ok(ApiResponse.ok("내 점수 및 이력 조회 성공", response));
    }
}
