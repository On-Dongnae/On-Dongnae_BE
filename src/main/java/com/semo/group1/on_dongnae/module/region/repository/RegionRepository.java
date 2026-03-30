package com.semo.group1.on_dongnae.module.region.repository;

import com.semo.group1.on_dongnae.entity.Region;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegionRepository extends JpaRepository<Region, Long> {
    // 기본적으로 ID로 찾는 기능(findById)을 자동으로 제공해주므로,
    // 당장 추가로 적을 메서드는 없습니다. 텅 비워두셔도 됩니다!
}