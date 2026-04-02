package com.semo.group1.on_dongnae.module.admin.dto;

import com.semo.group1.on_dongnae.entity.enums.MissionType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class AdminMissionRequestDto {
    private String name;
    private String description;
    private MissionType type;
    private Integer pointAmount;
    private LocalDate startDate;
    private LocalDate endDate;
}
