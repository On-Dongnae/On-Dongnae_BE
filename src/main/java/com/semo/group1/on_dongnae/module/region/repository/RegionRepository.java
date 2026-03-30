package com.semo.group1.on_dongnae.module.region.repository;

import com.semo.group1.on_dongnae.entity.Region;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegionRepository extends JpaRepository<Region, Long> {
    // 기본적으로 ID로 찾는 기능(findById)만을 필요

}