package com.semo.group1.on_dongnae.module.admin.service;

import com.semo.group1.on_dongnae.entity.MissionVerification;
import com.semo.group1.on_dongnae.entity.UserMission;
import com.semo.group1.on_dongnae.entity.enums.UserMissionStatus;
import com.semo.group1.on_dongnae.global.exception.CustomException;
import com.semo.group1.on_dongnae.global.exception.ErrorCode;
import com.semo.group1.on_dongnae.module.verification.repository.MissionVerificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminVerificationService {

    private final MissionVerificationRepository verificationRepository;

    @Transactional
    public void approveVerification(Long verificationId) {
        MissionVerification verification = verificationRepository.findById(verificationId)
                .orElseThrow(() -> new CustomException(ErrorCode.VERIFICATION_NOT_FOUND));

        verification.approve();

        // 연관된 UserMission 상태도 업데이트
        UserMission userMission = verification.getUserMission();
        userMission.updateStatus(UserMissionStatus.VERIFIED);
    }

    @Transactional
    public void rejectVerification(Long verificationId, String reason) {
        MissionVerification verification = verificationRepository.findById(verificationId)
                .orElseThrow(() -> new CustomException(ErrorCode.VERIFICATION_NOT_FOUND));

        verification.reject(reason);

        // 연관된 UserMission 상태도 업데이트
        UserMission userMission = verification.getUserMission();
        userMission.updateStatus(UserMissionStatus.REJECTED);
    }
}
