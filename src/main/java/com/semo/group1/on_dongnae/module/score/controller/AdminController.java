package com.semo.group1.on_dongnae.module.score.controller;

import com.semo.group1.on_dongnae.global.common.ApiResponse; // 프로젝트 공용 응답 형식
import com.semo.group1.on_dongnae.module.score.cache.RankingCache; // 캐시 클래스
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/rankings") // 관리자용 경로
@RequiredArgsConstructor
public class AdminController {
    private final RankingCache rankingCache; // 창고(Cache)를 주입받습니다.

    // 프론트엔드에 API 전송
    @GetMapping("/status")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getRankingStatus() {

        // 1. Cache에서 데이터 추출
        LocalDateTime lastUserUpdate = rankingCache.getLastUserUpdate();
        int lastUserUpdateCount = rankingCache.getLastUserUpdateCount();

        // 2. Map으로 포장 for 가독성
        Map<String, Object> data = new HashMap<>();
        data.put("lastUserUpdate", lastUserUpdate);
        data.put("lastUserUpdateCount", lastUserUpdateCount);

        // 3. ApiResponse return <= 프론트의 요청
        return ResponseEntity.ok(ApiResponse.ok("랭킹 상태 정보입니다.", data));
    }
}
