package com.semo.group1.on_dongnae.module.region.dto;

import com.semo.group1.on_dongnae.entity.Region;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RegionResponseDto {
    private Long id;
    private String city;
    private String district;

    public static RegionResponseDto fromEntity(Region region) {
        return RegionResponseDto.builder()
                .id(region.getId())
                .city(region.getCity())
                .district(region.getDistrict())
                .build();
    }
}
