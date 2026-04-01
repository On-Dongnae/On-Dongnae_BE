package com.semo.group1.on_dongnae.module.mission.service;

import com.semo.group1.on_dongnae.entity.Mission;
import com.semo.group1.on_dongnae.entity.User;
import com.semo.group1.on_dongnae.entity.UserMission;
import com.semo.group1.on_dongnae.entity.enums.MissionType;
import com.semo.group1.on_dongnae.global.exception.CustomException;
import com.semo.group1.on_dongnae.global.exception.ErrorCode;
import com.semo.group1.on_dongnae.global.security.SecurityUtil;
import com.semo.group1.on_dongnae.module.mission.dto.MissionDto;
import com.semo.group1.on_dongnae.module.mission.dto.UserMissionDto;
import com.semo.group1.on_dongnae.module.mission.repository.MissionRepository;
import com.semo.group1.on_dongnae.module.mission.repository.UserMissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MissionService {

    private final MissionRepository missionRepository;
    private final UserMissionRepository userMissionRepository;
    private final SecurityUtil securityUtil;

    // 1. 활성화된 미션 전체 조회
    public List<MissionDto> getAllActiveMissions() {
        return missionRepository.findByIsActiveTrue()
                .stream()
                .map(MissionDto::fromEntity)
                .collect(Collectors.toList());
    }

    // 2. 미션 상세 조회
    public MissionDto getMissionById(Long id) {
        Mission mission = missionRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.MISSION_NOT_FOUND));
        
        return MissionDto.fromEntity(mission);
    }

    // 3. 오늘의 미션 배정 (INITIAL 타입 중 랜덤 1개)
    @Transactional
    public UserMissionDto assignDailyMission() {
        User user = securityUtil.getCurrentUser();
        LocalDate today = LocalDate.now();

        // 중복 배정 체크 (오늘은 이미 미션을 받았는지)
        if (userMissionRepository.existsByUserAndAssignedDate(user, today)) {
            throw new CustomException(ErrorCode.ALREADY_ASSIGNED);
        }

        // INITIAL 타입의 활성화된 미션 중 랜덤 1개 가져오기
        Mission mission = missionRepository.findRandomActiveMissionByType(MissionType.INITIAL.name())
                .orElseThrow(() -> new CustomException(ErrorCode.MISSION_NOT_FOUND));

        // 유저 미션 생성
        UserMission userMission = UserMission.builder()
                .user(user)
                .mission(mission)
                .assignedDate(today)
                .build();

        userMissionRepository.save(userMission);

        return UserMissionDto.fromEntity(userMission);
    }

    // 4. 내 미션 목록 조회
    public List<UserMissionDto> getMyMissions() {
        User user = securityUtil.getCurrentUser();
        
        return userMissionRepository.findByUser(user)
                .stream()
                .map(UserMissionDto::fromEntity)
                .collect(Collectors.toList());
    }
}
