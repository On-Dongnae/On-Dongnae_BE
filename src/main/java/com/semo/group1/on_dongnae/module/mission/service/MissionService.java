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

    // 3. 오늘의 미션 일괄 배정 (INITIAL 타입 모두 배정)
    @Transactional
    public List<UserMissionDto> assignDailyMission() {
        User user = securityUtil.getCurrentUser();
        LocalDate today = LocalDate.now();

        // 1. 해당 유저가 오늘 배정받은 미션 목록 조회
        List<UserMission> alreadyAssigned = userMissionRepository.findByUserAndAssignedDate(user, today);
        java.util.Set<Long> assignedMissionIds = alreadyAssigned.stream()
                .map(um -> um.getMission().getId())
                .collect(Collectors.toSet());

        // 2. 전체 활성 INITIAL 미션 조회
        List<Mission> allDailyMissions = missionRepository.findByIsActiveTrueAndType(MissionType.INITIAL);

        // 3. 아직 배정되지 않은 미션 필터링
        List<Mission> missionsToAssign = allDailyMissions.stream()
                .filter(m -> !assignedMissionIds.contains(m.getId()))
                .collect(Collectors.toList());

        // 4. 새 미션 배정
        List<UserMission> newlyAssigned = new java.util.ArrayList<>();
        for (Mission mission : missionsToAssign) {
            UserMission userMission = UserMission.builder()
                    .user(user)
                    .mission(mission)
                    .assignedDate(today)
                    .build();
            newlyAssigned.add(userMission);
        }

        if (!newlyAssigned.isEmpty()) {
            userMissionRepository.saveAll(newlyAssigned);
        } else if (alreadyAssigned.isEmpty()) {
            // 오늘 배정받은 것도 없고 배정할 것도 없으면 미션이 없는 것
            throw new CustomException(ErrorCode.MISSION_NOT_FOUND);
        } else {
            // 새로 배정할 건 없는데 이미 받은 게 있으면 그대로 기존 목록 반환
            // (혹은 기존처럼 ALREADY_ASSIGNED 에러를 던질 수 있지만, 목록을 보여주는 게 더 자연스럽습니다)
        }

        // 5. 이미 받은 미션 + 오늘 새로 받은 미션 합쳐서 반환
        List<UserMission> result = new java.util.ArrayList<>(alreadyAssigned);
        result.addAll(newlyAssigned);

        return result.stream()
                .map(UserMissionDto::fromEntity)
                .collect(Collectors.toList());
    }

    // 4. 내 미션 목록 조회 (옵션: 타입별 필터링)
    public List<UserMissionDto> getMyMissions(MissionType type) {
        User user = securityUtil.getCurrentUser();
        
        List<UserMission> userMissions;
        if (type != null) {
            userMissions = userMissionRepository.findByUserAndMission_Type(user, type);
        } else {
            userMissions = userMissionRepository.findByUser(user);
        }
        
        return userMissions.stream()
                .map(UserMissionDto::fromEntity)
                .collect(Collectors.toList());
    }
}
