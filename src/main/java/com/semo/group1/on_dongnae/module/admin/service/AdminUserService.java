package com.semo.group1.on_dongnae.module.admin.service;

import com.semo.group1.on_dongnae.entity.User;
import com.semo.group1.on_dongnae.entity.UserSuspension;
import com.semo.group1.on_dongnae.entity.enums.UserStatus;
import com.semo.group1.on_dongnae.global.exception.CustomException;
import com.semo.group1.on_dongnae.global.exception.ErrorCode;
import com.semo.group1.on_dongnae.module.admin.repository.UserSuspensionRepository;
import com.semo.group1.on_dongnae.module.user.repository.UserRepository;
import com.semo.group1.on_dongnae.module.admin.dto.AdminUserResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminUserService {

    private final UserRepository userRepository;
    private final UserSuspensionRepository suspensionRepository;

    @Transactional
    public void suspendUser(Long userId, String reason) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        if (user.getStatus() == UserStatus.SUSPENDED) {
            return; // 이미 정지된 상태면 무시
        }

        user.updateStatus(UserStatus.SUSPENDED);

        UserSuspension suspension = UserSuspension.builder()
                .user(user)
                .reason(reason)
                .suspendedAt(LocalDateTime.now())
                .build();

        suspensionRepository.save(suspension);
    }

    @Transactional
    public void activateUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        user.updateStatus(UserStatus.ACTIVE);
    }

    @Transactional(readOnly = true)
    public List<AdminUserResponseDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(user -> AdminUserResponseDto.builder()
                        .userId(user.getId())
                        .email(user.getEmail())
                        .nickname(user.getNickname())
                        .role(user.getRole())
                        .status(user.getStatus())
                        .profileImageUrl(user.getProfileImageUrl())
                        .totalScore(user.getTotalScore())
                        .regionName(user.getRegion().getCity() + " " + user.getRegion().getDistrict())
                        .createdAt(user.getCreatedAt())
                        .build())
                .collect(Collectors.toList());
    }
}
