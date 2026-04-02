package com.semo.group1.on_dongnae.module.admin.controller;

import com.semo.group1.on_dongnae.global.common.ApiResponse;
import com.semo.group1.on_dongnae.module.admin.dto.AdminMissionRequestDto;
import com.semo.group1.on_dongnae.module.admin.service.AdminMissionService;
import lombok.RequiredArgsConstructor;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Admin (관리자)", description = "관리자 전용 기능 (미션 관리)")
@RestController
@RequestMapping("/api/admin/missions")
@RequiredArgsConstructor
public class AdminMissionController {

    private final AdminMissionService adminMissionService;

    @Operation(summary = "미션 등록", description = "새로운 일일 미션을 시스템에 등록합니다.")
    @PostMapping
    public ResponseEntity<ApiResponse<Void>> createMission(@RequestBody AdminMissionRequestDto request) {
        adminMissionService.createMission(request);
        return ResponseEntity.ok(ApiResponse.created("새 기록 미션이 등록되었습니다."));
    }

    @Operation(summary = "미션 수정", description = "기존 미션의 정보(보상 점수 등)를 수정합니다.")
    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> updateMission(
            @PathVariable Long id,
            @RequestBody AdminMissionRequestDto request) {
        adminMissionService.updateMission(id, request);
        return ResponseEntity.ok(ApiResponse.ok("미션 정보가 수정되었습니다."));
    }
}
