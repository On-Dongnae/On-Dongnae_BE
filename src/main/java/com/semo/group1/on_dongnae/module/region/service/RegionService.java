package com.semo.group1.on_dongnae.module.region.service;

import com.semo.group1.on_dongnae.module.region.dto.RegionDto;
import com.semo.group1.on_dongnae.module.region.repository.RegionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RegionService {

    private final RegionRepository regionRepository;

    public List<RegionDto> getAllRegions() {
        return regionRepository.findAll()
                .stream()
                .map(RegionDto::fromEntity)
                .collect(Collectors.toList());
    }

    public List<RegionDto> searchRegions(String keyword) {
        return regionRepository.findByDistrictContaining(keyword)
                .stream()
                .map(RegionDto::fromEntity)
                .collect(Collectors.toList());
    }
}
