package com.semo.group1.on_dongnae.module.user.dto;

import com.semo.group1.on_dongnae.entity.User;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserProfileDto {
    private Long id;
    private String email;
    private String nickname;
    private String profileImageUrl;
    private String regionName;
    private Integer totalScore;

    public static UserProfileDto fromEntity(User user) {
        return UserProfileDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .profileImageUrl(user.getProfileImageUrl())
                .regionName(user.getRegion().getDistrict())
                .totalScore(user.getTotalScore())
                .build();
    }
}
