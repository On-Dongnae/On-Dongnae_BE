package com.semo.group1.on_dongnae.module.mission.controller;

import com.semo.group1.on_dongnae.global.common.ApiResponse;
import com.semo.group1.on_dongnae.module.mission.dto.UserMissionDto;
import com.semo.group1.on_dongnae.module.mission.service.MissionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Mission (미션)", description = "나에게 배정된 개인 미션 관리")
@RestController
@RequestMapping("/api/users/me/missions")
@RequiredArgsConstructor
public class UserMissionController {

    private final MissionService missionService;

    @Operation(summary = "내 미션 목록 조회", description = "내가 현재 수행 중인 미션 목록을 조회합니다. 타입별 필터링이 가능합니다.")
    @GetMapping
    public ResponseEntity<ApiResponse<List<UserMissionDto>>> getMyMissions(
            @RequestParam(name = "type", required = false) com.semo.group1.on_dongnae.entity.enums.MissionType type) {
        return ResponseEntity.ok(ApiResponse.ok("내 미션 목록을 성공적으로 불러왔습니다.", missionService.getMyMissions(type)));
    }
}
