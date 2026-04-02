package com.semo.group1.on_dongnae.module.admin.service;

import com.semo.group1.on_dongnae.entity.User;
import com.semo.group1.on_dongnae.entity.UserSuspension;
import com.semo.group1.on_dongnae.entity.enums.UserStatus;
import com.semo.group1.on_dongnae.global.exception.CustomException;
import com.semo.group1.on_dongnae.global.exception.ErrorCode;
import com.semo.group1.on_dongnae.module.admin.repository.UserSuspensionRepository;
import com.semo.group1.on_dongnae.module.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

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
}
