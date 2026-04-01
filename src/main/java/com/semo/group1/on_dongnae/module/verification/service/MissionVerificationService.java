package com.semo.group1.on_dongnae.module.verification.service;

import com.semo.group1.on_dongnae.entity.MissionVerification;
import com.semo.group1.on_dongnae.entity.User;
import com.semo.group1.on_dongnae.entity.UserMission;
import com.semo.group1.on_dongnae.entity.VerificationImage;
import com.semo.group1.on_dongnae.entity.enums.UserMissionStatus;
import com.semo.group1.on_dongnae.entity.enums.VerificationSource;
import com.semo.group1.on_dongnae.entity.enums.VerificationStatus;
import com.semo.group1.on_dongnae.global.exception.CustomException;
import com.semo.group1.on_dongnae.global.exception.ErrorCode;
import com.semo.group1.on_dongnae.global.security.SecurityUtil;
import com.semo.group1.on_dongnae.module.mission.repository.UserMissionRepository;
import com.semo.group1.on_dongnae.module.verification.dto.VerificationDto;
import com.semo.group1.on_dongnae.module.verification.repository.MissionVerificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MissionVerificationService {

    private final MissionVerificationRepository missionVerificationRepository;
    private final UserMissionRepository userMissionRepository;
    private final S3UploadService s3UploadService;
    private final SecurityUtil securityUtil;

    @Transactional
    public VerificationDto verifyMission(Long userMissionId, String content, List<MultipartFile> images) {
        User user = securityUtil.getCurrentUser();

        UserMission userMission = userMissionRepository.findById(userMissionId)
                .orElseThrow(() -> new CustomException(ErrorCode.MISSION_NOT_FOUND));

        // 배정받은 미션이 내 것인지 확인
        if (!userMission.getUser().getId().equals(user.getId())) {
            throw new CustomException(ErrorCode.MISSION_NOT_FOUND); 
        }

        // 중복 인증 요청 확인
        if (missionVerificationRepository.existsByUserMission(userMission)) {
            throw new CustomException(ErrorCode.ALREADY_VERIFIED);
        }

        MissionVerification verification = MissionVerification.builder()
                .userMission(userMission)
                .content(content)
                .status(VerificationStatus.PENDING) // 관리자/AI 승인 대기 상태로 시작
                .images(new ArrayList<>())
                .build();

        // 이미지 업로드 수행
        if (images != null && !images.isEmpty()) {
            int order = 1;
            for (MultipartFile file : images) {
                if (file.isEmpty()) continue;
                try {
                    String imageUrl = s3UploadService.uploadFile(file, "verifications/" + userMissionId);
                    VerificationImage vi = VerificationImage.builder()
                            .missionVerification(verification)
                            .imageUrl(imageUrl)
                            .imageOrder(order++)
                            .build();
                    verification.getImages().add(vi);
                } catch (IOException e) {
                    log.error("S3 파일 업로드 중 오류 발생", e);
                    throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
                }
            }
        }

        missionVerificationRepository.save(verification);

        // 연관된 UserMission의 상태를 대기 상태(PENDING_VERIFICATION)로 업데이트
        userMission.updateStatus(UserMissionStatus.PENDING_VERIFICATION);

        return VerificationDto.fromEntity(verification);
    }
}
