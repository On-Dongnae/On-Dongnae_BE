package com.semo.group1.on_dongnae.module.admin.service;

import com.semo.group1.on_dongnae.entity.MissionVerification;
import com.semo.group1.on_dongnae.entity.User;
import com.semo.group1.on_dongnae.entity.UserMission;
import com.semo.group1.on_dongnae.entity.enums.UserMissionStatus;
import com.semo.group1.on_dongnae.entity.enums.VerificationStatus;
import com.semo.group1.on_dongnae.global.exception.CustomException;
import com.semo.group1.on_dongnae.global.exception.ErrorCode;
import com.semo.group1.on_dongnae.module.admin.dto.AiVerificationRequestDto;
import com.semo.group1.on_dongnae.module.score.service.ScoreService;
import com.semo.group1.on_dongnae.module.verification.dto.VerificationDto;
import com.semo.group1.on_dongnae.module.verification.repository.MissionVerificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminVerificationService {

    private final MissionVerificationRepository verificationRepository;
    private final ScoreService scoreService;

    @Transactional
    public void approveVerification(Long verificationId) {
        MissionVerification verification = verificationRepository.findById(verificationId)
                .orElseThrow(() -> new CustomException(ErrorCode.VERIFICATION_NOT_FOUND));

        verification.approve();

        UserMission userMission = verification.getUserMission();
        userMission.updateStatus(UserMissionStatus.VERIFIED);

        // 포인트 자동 지급
        grantMissionScore(userMission);
    }

    @Transactional
    public void rejectVerification(Long verificationId, String reason) {
        MissionVerification verification = verificationRepository.findById(verificationId)
                .orElseThrow(() -> new CustomException(ErrorCode.VERIFICATION_NOT_FOUND));

        verification.reject(reason);

        UserMission userMission = verification.getUserMission();
        userMission.updateStatus(UserMissionStatus.REJECTED);
    }

    @Transactional(readOnly = true)
    public List<VerificationDto> getVerificationsByStatus(VerificationStatus status) {
        return verificationRepository.findByStatusOrderByVerifiedAtDesc(status).stream()
                .map(VerificationDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public void saveAiResult(Long verificationId, AiVerificationRequestDto request) {
        MissionVerification verification = verificationRepository.findById(verificationId)
                .orElseThrow(() -> new CustomException(ErrorCode.VERIFICATION_NOT_FOUND));

        UserMission userMission = verification.getUserMission();

        if (request.getStatus() == VerificationStatus.APPROVED) {
            verification.approve();
            userMission.updateStatus(UserMissionStatus.VERIFIED);
            // AI 승인 시에도 포인트 자동 지급
            grantMissionScore(userMission);
        } else {
            verification.reject(request.getReason());
            userMission.updateStatus(UserMissionStatus.REJECTED);
        }
    }

    /**
     * 미션 완료 시 포인트를 자동 지급하는 내부 메소드
     */
    private void grantMissionScore(UserMission userMission) {
        User user = userMission.getUser();
        Long regionId = user.getRegion().getId();
        Integer pointAmount = userMission.getMission().getPointAmount();
        Long missionId = userMission.getMission().getId();

        // Score 테이블에 이력 기록
        scoreService.addMissionScore(user.getId(), regionId, pointAmount, missionId);
        // User의 총 점수도 업데이트
        user.addScore(pointAmount);
    }
}

