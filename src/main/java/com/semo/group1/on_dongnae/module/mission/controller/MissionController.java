package com.semo.group1.on_dongnae.module.mission.controller;

import com.semo.group1.on_dongnae.global.common.ApiResponse;
import com.semo.group1.on_dongnae.module.mission.dto.MissionDto;
import com.semo.group1.on_dongnae.module.mission.dto.UserMissionDto;
import com.semo.group1.on_dongnae.module.mission.service.MissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/missions")
@RequiredArgsConstructor
public class MissionController {

    private final MissionService missionService;

    // 1. 활성화된 모든 미션 조회
    @GetMapping
    public ResponseEntity<ApiResponse<List<MissionDto>>> getAllMissions() {
        return ResponseEntity.ok(ApiResponse.ok("미션 목록을 성공적으로 불러왔습니다.", missionService.getAllActiveMissions()));
    }

    // 2. 미션 상세 조회
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<MissionDto>> getMissionById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok("미션 정보를 성공적으로 불러왔습니다.", missionService.getMissionById(id)));
    }

    // 3. 오늘의 미션 조회 (첫 접속 시 자동 배정)
    @GetMapping("/today")
    public ResponseEntity<ApiResponse<List<UserMissionDto>>> getTodayMissions() {
        return ResponseEntity.ok(ApiResponse.ok("오늘의 미션 목록입니다.", missionService.getTodayMissions()));
    }

    // 4. 오늘의 미션 일괄 배정 (수동)
    @PostMapping("/daily")
    public ResponseEntity<ApiResponse<List<UserMissionDto>>> assignDailyMission() {
        return ResponseEntity.ok(ApiResponse.created("오늘의 미션이 성공적으로 모두 배정되었습니다.", missionService.assignDailyMission()));
    }
}
