package com.semo.group1.on_dongnae.module.score.repository;

import com.semo.group1.on_dongnae.entity.Region;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegionRepository extends JpaRepository<Region, Long> {
    // 단순 CRUD
}
