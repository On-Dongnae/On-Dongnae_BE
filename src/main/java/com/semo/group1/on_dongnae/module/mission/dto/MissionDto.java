package com.semo.group1.on_dongnae.module.mission.dto;

import com.semo.group1.on_dongnae.entity.Mission;
import com.semo.group1.on_dongnae.entity.enums.MissionType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class MissionDto {
    private Long id;
    private MissionType type;
    private String name;
    private String description;
    private Integer pointAmount;
    private LocalDate startDate;
    private LocalDate endDate;
    private Boolean isActive;

    public static MissionDto fromEntity(Mission mission) {
        return MissionDto.builder()
                .id(mission.getId())
                .type(mission.getType())
                .name(mission.getName())
                .description(mission.getDescription())
                .pointAmount(mission.getPointAmount())
                .startDate(mission.getStartDate())
                .endDate(mission.getEndDate())
                .isActive(mission.getIsActive())
                .build();
    }
}
