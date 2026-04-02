package com.semo.group1.on_dongnae.module.admin.service;

import com.semo.group1.on_dongnae.entity.MissionVerification;
import com.semo.group1.on_dongnae.entity.UserMission;
import com.semo.group1.on_dongnae.entity.enums.UserMissionStatus;
import com.semo.group1.on_dongnae.entity.enums.VerificationStatus;
import com.semo.group1.on_dongnae.global.exception.CustomException;
import com.semo.group1.on_dongnae.global.exception.ErrorCode;
import com.semo.group1.on_dongnae.module.admin.dto.AiVerificationRequestDto;
import com.semo.group1.on_dongnae.module.mission.repository.UserMissionRepository;
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
    private final UserMissionRepository userMissionRepository;

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

        // AI 결과 반영 (엔티티에 직접 적용하거나 전용 메소드 활용)
        // 💡 MissionVerification 엔티티에 AI 전용 업데이트 로직을 추가하는 것이 좋음
        if (request.getStatus() == VerificationStatus.APPROVED) {
            verification.approve();
        } else {
            verification.reject(request.getReason());
        }

        // AI 결과 기록 (신뢰도 등)
        // 💡 엔티티 필드에 직접 접근 (필요하면 setter/메소드 추가)
        // 여기서는 임시로 리플렉션 대신 직접 필드 업데이트를 가정 (수동 검증 시에는 source가 바뀌므로 주의)

        userMissionRepository.save(verification.getUserMission());
    }
}
