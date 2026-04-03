package com.semo.group1.on_dongnae.module.mission.repository;

import com.semo.group1.on_dongnae.entity.Mission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.semo.group1.on_dongnae.entity.enums.MissionType;

import java.util.List;
import java.util.Optional;

public interface MissionRepository extends JpaRepository<Mission, Long> {
    
    // 활성화된 모든 미션 조회
    List<Mission> findByIsActiveTrue();
    
    // 특정 타입의 활성화된 미션 전체 조회
    List<Mission> findByIsActiveTrueAndType(MissionType type);
}
