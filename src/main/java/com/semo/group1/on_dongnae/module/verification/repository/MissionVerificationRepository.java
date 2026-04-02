package com.semo.group1.on_dongnae.module.verification.repository;

import com.semo.group1.on_dongnae.entity.MissionVerification;
import com.semo.group1.on_dongnae.entity.UserMission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MissionVerificationRepository extends JpaRepository<MissionVerification, Long> {
    Optional<MissionVerification> findByUserMission(UserMission userMission);
    boolean existsByUserMission(UserMission userMission);
}
