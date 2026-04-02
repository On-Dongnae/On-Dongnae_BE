package com.semo.group1.on_dongnae.module.admin.controller;

import com.semo.group1.on_dongnae.global.common.ApiResponse;
import com.semo.group1.on_dongnae.global.security.SecurityUtil;
import com.semo.group1.on_dongnae.module.admin.dto.AdminScoreAdjustDto;
import com.semo.group1.on_dongnae.module.score.service.ScoreService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Admin (관리자)", description = "관리자 전용 기능 (점수 관리)")
@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
public class AdminScoreController {

    private final ScoreService scoreService;
    private final SecurityUtil securityUtil;

    @Operation(summary = "유저 점수 수동 조정", description = "회원에게 수동으로 점수를 부여하거나 회수합니다.")
    @PostMapping("/{userId}/score")
    public ResponseEntity<ApiResponse<Void>> adjustScore(
            @PathVariable("userId") Long userId,
            @RequestBody AdminScoreAdjustDto request) {
        Long adminId = securityUtil.getCurrentUser().getId();
        scoreService.adjustScoreByAdmin(userId, request.getRegionId(), request.getAmount(), adminId);
        return ResponseEntity.ok(ApiResponse.ok("점수가 수동으로 조정되었습니다."));
    }
}
