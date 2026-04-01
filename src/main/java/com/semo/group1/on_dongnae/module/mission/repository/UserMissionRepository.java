package com.semo.group1.on_dongnae.module.mission.repository;

import com.semo.group1.on_dongnae.entity.User;
import com.semo.group1.on_dongnae.entity.UserMission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface UserMissionRepository extends JpaRepository<UserMission, Long> {
    
    // 특정 유저의 내 미션 목록 조회
    List<UserMission> findByUser(User user);
    
    // 특정 일자(오늘)에 해당 유저가 배정받은 미션이 이미 있는지 확인
    boolean existsByUserAndAssignedDate(User user, LocalDate assignedDate);
}
