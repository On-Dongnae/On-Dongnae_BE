package com.semo.group1.on_dongnae.module.mission.dto;

import com.semo.group1.on_dongnae.entity.UserMission;
import com.semo.group1.on_dongnae.entity.enums.UserMissionStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Builder
public class UserMissionDto {
    private Long id;
    private MissionDto mission;
    private UserMissionStatus status;
    private LocalDate assignedDate;
    private LocalDateTime completedAt;

    public static UserMissionDto fromEntity(UserMission userMission) {
        return UserMissionDto.builder()
                .id(userMission.getId())
                .mission(MissionDto.fromEntity(userMission.getMission()))
                .status(userMission.getStatus())
                .assignedDate(userMission.getAssignedDate())
                .completedAt(userMission.getCompletedAt())
                .build();
    }
}
