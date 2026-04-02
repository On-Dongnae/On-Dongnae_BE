package com.semo.group1.on_dongnae.module.score.controller;

import com.semo.group1.on_dongnae.global.common.ApiResponse;
import com.semo.group1.on_dongnae.module.score.dto.ScoreHistoryDto;
import com.semo.group1.on_dongnae.module.score.service.ScoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
// import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;


@RestController
@RequestMapping("/api/scores")
@RequiredArgsConstructor
public class ScoreController {
    private final ScoreService scoreService;

    @GetMapping("/me")
    // 내 점수 불러오기
    public ResponseEntity<ApiResponse<List<ScoreHistoryDto>>> getMyScoreHistory(
            @AuthenticationPrincipal String userIdStr) {
        Long userId = Long.parseLong(userIdStr);
        List<ScoreHistoryDto> history = scoreService.getMyScoreHistory(userId);

        return ResponseEntity.ok(ApiResponse.ok("점수 내역을 성공적으로 불러왔습니다.", history));

    }

}
