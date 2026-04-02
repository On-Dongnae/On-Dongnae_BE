package com.semo.group1.on_dongnae.module.admin.controller;

import com.semo.group1.on_dongnae.global.common.ApiResponse;
import com.semo.group1.on_dongnae.module.admin.dto.AdminMissionRequestDto;
import com.semo.group1.on_dongnae.module.admin.service.AdminMissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/missions")
@RequiredArgsConstructor
public class AdminMissionController {

    private final AdminMissionService adminMissionService;

    @PostMapping
    public ResponseEntity<ApiResponse<Void>> createMission(@RequestBody AdminMissionRequestDto request) {
        adminMissionService.createMission(request);
        return ResponseEntity.ok(ApiResponse.created("새 기록 미션이 등록되었습니다."));
    }
}
