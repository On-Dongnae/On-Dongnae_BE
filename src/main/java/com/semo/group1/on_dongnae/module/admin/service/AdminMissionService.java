package com.semo.group1.on_dongnae.module.admin.service;

import com.semo.group1.on_dongnae.entity.Mission;
import com.semo.group1.on_dongnae.global.exception.CustomException;
import com.semo.group1.on_dongnae.global.exception.ErrorCode;
import com.semo.group1.on_dongnae.module.mission.repository.MissionRepository;
import com.semo.group1.on_dongnae.module.admin.dto.AdminMissionRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminMissionService {

    private final MissionRepository missionRepository;

    @Transactional
    public void createMission(AdminMissionRequestDto request) {
        Mission mission = Mission.builder()
                .name(request.getName())
                .description(request.getDescription())
                .type(request.getType())
                .pointAmount(request.getPointAmount())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .isActive(true)
                .build();

        missionRepository.save(mission);
    }

    @Transactional
    public void updateMission(Long missionId, AdminMissionRequestDto request) {
        Mission mission = missionRepository.findById(missionId)
                .orElseThrow(() -> new CustomException(ErrorCode.INTERNAL_SERVER_ERROR));

        mission.update(
                request.getName(),
                request.getDescription(),
                request.getType(),
                request.getPointAmount(),
                request.getStartDate(),
                request.getEndDate()
        );
    }
}
