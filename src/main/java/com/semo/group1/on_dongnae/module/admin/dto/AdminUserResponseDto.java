package com.semo.group1.on_dongnae.module.admin.dto;

import com.semo.group1.on_dongnae.entity.enums.Role;
import com.semo.group1.on_dongnae.entity.enums.UserStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class AdminUserResponseDto {
    private Long userId;
    private String email;
    private String nickname;
    private Role role;
    private UserStatus status;
    private String profileImageUrl;
    private Integer totalScore;
    private String regionName;
    private LocalDateTime createdAt;
}
