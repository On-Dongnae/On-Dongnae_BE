package com.semo.group1.on_dongnae.module.mission.repository;

import com.semo.group1.on_dongnae.entity.Mission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MissionRepository extends JpaRepository<Mission, Long> {
    
    // 활성화된 모든 미션 조회
    List<Mission> findByIsActiveTrue();
    
    // 특정 타입의 활성화된 미션을 랜덤으로 1개 가져오는 쿼리 (PostgreSQL 문법: RANDOM())
    @Query(value = "SELECT * FROM missions m WHERE m.is_active = true AND m.type = :type ORDER BY RANDOM() LIMIT 1", nativeQuery = true)
    Optional<Mission> findRandomActiveMissionByType(@Param("type") String type);
}
