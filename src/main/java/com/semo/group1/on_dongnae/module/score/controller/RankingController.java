package com.semo.group1.on_dongnae.module.score.controller;


import com.semo.group1.on_dongnae.module.score.dto.RegionRanking;
import com.semo.group1.on_dongnae.module.score.dto.UserRanking;
import com.semo.group1.on_dongnae.module.score.repository.UserRepository;
import com.semo.group1.on_dongnae.module.score.service.ScoreService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/rankings")
public class RankingController {
    private final ScoreService scoreService;

    @GetMapping("/user")
    // 기본 상위 n명 = 10명
    // ex) /api/rankings/regions 끝 ? => 상위 10명만 return
    public ResponseEntity<List<UserRanking>> getUserRankings(
            @RequestParam(defaultValue = "10") int n) {
        return ResponseEntity.ok(scoreService.getTopNUserRanking(n));
    }
    // 기본 상위 n개 = 10개
    @GetMapping("/region")
    public ResponseEntity<List<RegionRanking>> getRegionRankings(
            @RequestParam(defaultValue = "10") int n) {
        return ResponseEntity.ok(scoreService.getTopNRegionRanking(n));
    }

    @GetMapping("/me")
    // 내 등수 확인하기
    public ResponseEntity<Long> getMyRanking(
            @AuthenticationPrincipal String userIdSrt) {
        Long userId = Long.parseLong(userIdSrt);
        Long myRank = scoreService.getMyRanking(userId);

        return ResponseEntity.ok(myRank);

    }

}
