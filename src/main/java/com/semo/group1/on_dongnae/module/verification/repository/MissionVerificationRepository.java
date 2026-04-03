package com.semo.group1.on_dongnae.module.verification.repository;

import com.semo.group1.on_dongnae.entity.MissionVerification;
import com.semo.group1.on_dongnae.entity.UserMission;
import com.semo.group1.on_dongnae.entity.enums.VerificationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MissionVerificationRepository extends JpaRepository<MissionVerification, Long> {
    Optional<MissionVerification> findByUserMission(UserMission userMission);
    boolean existsByUserMission(UserMission userMission);

    // 상태별 미션 인증 리스트 조회 (최신 검증일 순)
    List<MissionVerification> findByStatusOrderByVerifiedAtDesc(VerificationStatus status);
}
