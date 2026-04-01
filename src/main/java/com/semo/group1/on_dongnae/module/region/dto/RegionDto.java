package com.semo.group1.on_dongnae.module.region.dto;

import com.semo.group1.on_dongnae.entity.Region;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class RegionDto {

    private Long id;
    private String city;
    private String district;
    private BigDecimal latitude;
    private BigDecimal longitude;

    public static RegionDto fromEntity(Region region) {
        return RegionDto.builder()
                .id(region.getId())
                .city(region.getCity())
                .district(region.getDistrict())
                .latitude(region.getLatitude())
                .longitude(region.getLongitude())
                .build();
    }
}
