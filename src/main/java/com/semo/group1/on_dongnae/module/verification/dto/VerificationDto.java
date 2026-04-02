package com.semo.group1.on_dongnae.module.verification.dto;

import com.semo.group1.on_dongnae.entity.MissionVerification;
import com.semo.group1.on_dongnae.entity.enums.VerificationStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class VerificationDto {
    private Long id;
    private Long userMissionId;
    private VerificationStatus status;
    private String content;
    private List<String> imageUrls;
    private LocalDateTime verifiedAt;

    public static VerificationDto fromEntity(MissionVerification nv) {
        return VerificationDto.builder()
                .id(nv.getId())
                .userMissionId(nv.getUserMission() != null ? nv.getUserMission().getId() : null)
                .status(nv.getStatus())
                .content(nv.getContent())
                .imageUrls(nv.getImages() != null ? 
                    nv.getImages().stream()
                        .map(img -> img.getImageUrl())
                        .collect(Collectors.toList()) : null)
                .verifiedAt(nv.getVerifiedAt())
                .build();
    }
}
