package com.semo.group1.on_dongnae.module.mission.controller;

import com.semo.group1.on_dongnae.global.common.ApiResponse;
import com.semo.group1.on_dongnae.module.mission.dto.UserMissionDto;
import com.semo.group1.on_dongnae.module.mission.service.MissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/users/me/missions")
@RequiredArgsConstructor
public class UserMissionController {

    private final MissionService missionService;

    // 내 미션 목록 조회
    @GetMapping
    public ResponseEntity<ApiResponse<List<UserMissionDto>>> getMyMissions() {
        return ResponseEntity.ok(ApiResponse.ok("내 미션 목록을 성공적으로 불러왔습니다.", missionService.getMyMissions()));
    }
}
